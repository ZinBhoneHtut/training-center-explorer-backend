package com.zbh.tce.controller;

import com.zbh.tce.entity.RefreshToken;
import com.zbh.tce.exception.TokenRefreshException;
import com.zbh.tce.security.dto.AuthRequest;
import com.zbh.tce.security.dto.AuthResponse;
import com.zbh.tce.security.dto.TokenRefreshRequest;
import com.zbh.tce.security.dto.TokenRefreshResponse;
import com.zbh.tce.security.jwt.JwtService;
import com.zbh.tce.security.service.RefreshTokenService;
import com.zbh.tce.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private static final String USER_AGENT_KEY = "user-agent";

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String accessToken = jwtService.generateJwtToken(authentication);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), httpServletRequest);
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .build();
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else {
            AuthResponse authResponse = AuthResponse.builder()
                    .errorMsg("Invalid username or password")
                    .errorCode(HttpStatus.BAD_REQUEST.name())
                    .build();
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request, HttpServletRequest httpServletRequest) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByTokenAndUserAgent(requestRefreshToken, httpServletRequest.getHeader(USER_AGENT_KEY))
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateTokenFromUsername(user.getName());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is invalid!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest httpServletRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserAndUserAgent(userId, httpServletRequest.getHeader(USER_AGENT_KEY));
        return ResponseEntity.ok("Successfully logout!");
    }
}