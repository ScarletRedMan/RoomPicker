package ru.dragonestia.picker.model.account;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    ADMIN, // account management
    NODE_MANAGEMENT, // create and remove nodes
    ;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
