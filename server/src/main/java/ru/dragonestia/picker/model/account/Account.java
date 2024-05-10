package ru.dragonestia.picker.model.account;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Account implements UserDetails {

    @Getter private final AccountId id;
    @Getter private final String username;
    private final String lowerUsername;
    @Getter @Setter private String password;
    private Set<Permission> permissions = new HashSet<>();
    @Getter @Setter private boolean locked = false;
    @Getter @Setter private boolean enabled = true;

    public Account(AccountId id, String password) {
        this.id = id;
        this.username = id.getValue();
        this.lowerUsername = username.toLowerCase();
        this.password = password;
    }

    @Override
    public Collection<Permission> getAuthorities() {
        return permissions;
    }

    public void setAuthorities(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
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
