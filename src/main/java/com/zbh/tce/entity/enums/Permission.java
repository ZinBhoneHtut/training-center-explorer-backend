package com.zbh.tce.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    MODERATOR_READ("moderator:read"),
    MODERATOR_WRITE("moderator:write"),
    MODERATOR_UPDATE("moderator:update"),
    MODERATOR_DELETE("moderator:delete");

    @Getter
    private final String permission;
}
