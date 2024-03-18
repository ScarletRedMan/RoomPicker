package ru.dragonestia.picker.cp.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.account.IAccount;
import ru.dragonestia.picker.api.model.account.ResponseAccount;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class Account implements IAccount, UserDetails {

    private final ResponseAccount original;
    private final RoomPickerClient client;
    private final Set<Permission> permissions;

    public Account(ResponseAccount original, RoomPickerClient client) {
        this.original = original;
        this.client = client;
        permissions = original.getPermissions().stream().map(Permission::new).collect(Collectors.toSet());
    }

    public @NotNull RoomPickerClient getClient() {
        return client;
    }

    @Override
    public Collection<Permission> getAuthorities() {
        return permissions;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !original.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull String getUsername() {
        return original.getUsername();
    }

    @Override
    public @NotNull String getPassword() {
        return original.getPassword();
    }

    @Override
    public @NotNull Set<String> getPermissions() {
        return original.getPermissions();
    }

    @Override
    public boolean isLocked() {
        return original.isLocked();
    }
}
