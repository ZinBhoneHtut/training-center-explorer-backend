package com.zbh.tce.security.service;

import com.zbh.tce.entity.RefreshToken;
import com.zbh.tce.exception.ResourceNotFoundException;
import com.zbh.tce.exception.TokenRefreshException;
import com.zbh.tce.repository.RefreshTokenRepository;
import com.zbh.tce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${app.security.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByTokenAndUserAgent(String token, String userAgent) {
        return refreshTokenRepository.findByTokenAndUserAgent(token, userAgent);
    }

    public RefreshToken createRefreshToken(Long userId, String userAgent) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserAgent(userAgent).orElse(
                RefreshToken.builder()
                        .user(userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+ userId)))
                        .userAgent(userAgent)
                        .build()
        );
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserAndUserAgent(Long userId, String userAgent) {
        return refreshTokenRepository.deleteByUserAndUserAgent(userRepository.findById(userId).get(), userAgent);
    }
}