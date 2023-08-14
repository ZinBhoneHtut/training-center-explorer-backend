package com.zbh.tce.repository;

import com.zbh.tce.entity.RefreshToken;
import com.zbh.tce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByUserAgent(String userAgent);

  Optional<RefreshToken> findByTokenAndUserAgent(String token, String userAgent);

  @Modifying
  int deleteByUserAndUserAgent(User user, String userAgent);
}