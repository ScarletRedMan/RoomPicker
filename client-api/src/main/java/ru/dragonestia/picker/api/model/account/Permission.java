package ru.dragonestia.picker.api.model.account;

public enum Permission {
    ADMIN, // account management
    NODE_MANAGEMENT, // create and remove nodes
    ;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
