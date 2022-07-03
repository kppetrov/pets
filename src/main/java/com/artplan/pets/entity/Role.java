package com.artplan.pets.entity;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ADMIN(Set.of(Permission.PET_READ, Permission.PET_WRITE)), 
    USER(Set.of(Permission.PET_READ, Permission.PET_WRITE));

    private final Set<Permission> permissions;

    private Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthoritys() {
        return getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());

    }
}
