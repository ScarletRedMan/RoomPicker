package ru.dragonestia.picker.api.model.account;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ResponseAccount implements IAccount {

    private String username;
    private String password;
    private Set<String> permissions;
    private boolean locked;

    public ResponseAccount() {}

    public ResponseAccount(String username, String password, Set<String> permissions, boolean locked) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.locked = locked;
    }

    @Override
    public @NotNull String getUsername() {
        return username;
    }

    @Override
    public @NotNull String getPassword() {
        return password;
    }

    @Override
    public @NotNull Set<String> getPermissions() {
        return permissions;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
