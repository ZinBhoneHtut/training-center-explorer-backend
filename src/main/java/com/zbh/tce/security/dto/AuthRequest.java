package com.zbh.tce.security.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthRequest {
    @NotNull(message = "* Username cannot be empty")
    private String username;
    @NotNull(message = "* Password cannot be empty")
    private String password;
}
