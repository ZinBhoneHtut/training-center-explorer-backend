package com.zbh.tce.security.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
