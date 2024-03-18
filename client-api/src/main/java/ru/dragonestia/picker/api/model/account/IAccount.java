package ru.dragonestia.picker.api.model.account;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IAccount {

    @NotNull String getUsername();

    @NotNull String getPassword();

    @NotNull Set<String> getPermissions();

    boolean isLocked();
}
