package com.zbh.tce.controller;

import com.zbh.tce.common.constant.UrlConstant;
import com.zbh.tce.entity.RefreshToken;
import com.zbh.tce.exception.TokenRefreshException;
import com.zbh.tce.security.dto.AuthRequest;
import com.zbh.tce.security.dto.AuthResponse;
import com.zbh.tce.security.dto.TokenRefreshRequest;
import com.zbh.tce.security.dto.TokenRefreshResponse;
import com.zbh.tce.security.jwt.JwtService;
import com.zbh.tce.security.service.AuthenticationService;
import com.zbh.tce.security.service.RefreshTokenService;
import com.zbh.tce.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(UrlConstant.API_V1_AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationService authenticationService;
    private static final String USER_AGENT_KEY = "user-agent";

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest httpServletRequest) {
        log.trace("Inside login method");
        String userAgent = httpServletRequest.getHeader(USER_AGENT_KEY);
        log.debug("User agent: {}", userAgent);
        AuthResponse authResponse = authenticationService.authenticate(authRequest, userAgent);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request, HttpServletRequest httpServletRequest) {
        log.trace("Inside refreshToken method");
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByTokenAndUserAgent(requestRefreshToken, httpServletRequest.getHeader(USER_AGENT_KEY))
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateTokenWithUsername(user.getName());
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