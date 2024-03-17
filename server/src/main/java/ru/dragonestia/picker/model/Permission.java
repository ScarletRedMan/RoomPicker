package ru.dragonestia.picker.model;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    ADMIN("admin");

    private final String id;

    Permission(String id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return id;
    }
}
