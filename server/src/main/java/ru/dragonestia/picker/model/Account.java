package ru.dragonestia.picker.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Account implements UserDetails {

    private final String username;
    private final String lowerUsername;
    private String password;
    private Set<Permission> permissions = new HashSet<>();
    private boolean locked = false;
    private boolean enabled = true;

    public Account(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.lowerUsername = username.toLowerCase();
        this.password = password;
    }

    @Override
    public Collection<Permission> getAuthorities() {
        return permissions;
    }

    @Contract("_ -> this")
    public @NotNull Account setAuthorities(@NotNull Set<Permission> permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Contract("_ -> this")
    public @NotNull Account setPassword(String value) {
        password = value;
        return this;
    }

    @Override
    public @NotNull String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Contract("_ -> this")
    public @NotNull Account setLocked(boolean value) {
        locked = value;
        return this;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Contract("_ -> this")
    public @NotNull Account setEnabled(boolean value) {
        enabled = value;
        return this;
    }

    @Override
    public int hashCode() {
        return lowerUsername.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof Account other) {
            return lowerUsername.equals(other.lowerUsername);
        }
        return false;
    }
}
