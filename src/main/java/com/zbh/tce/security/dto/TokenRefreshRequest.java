package com.zbh.tce.security.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TokenRefreshRequest {
    @NotNull(message = "Refresh token must be provided")
    private String refreshToken;
}
