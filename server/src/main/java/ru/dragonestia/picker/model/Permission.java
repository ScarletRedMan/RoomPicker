package ru.dragonestia.picker.model;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
