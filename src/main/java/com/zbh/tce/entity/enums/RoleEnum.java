package com.zbh.tce.entity.enums;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zbh.tce.entity.enums.Permission.*;

@RequiredArgsConstructor
public enum RoleEnum {
    USER(Collections.emptySet()),

    ADMIN(
            ImmutableSet.of(
                    ADMIN_READ,
                    ADMIN_WRITE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    ),

    MODERATOR(
            ImmutableSet.of(
                    MODERATOR_READ,
                    MODERATOR_WRITE,
                    MODERATOR_UPDATE,
                    MODERATOR_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.name()))
                    .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
