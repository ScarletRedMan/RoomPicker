package ru.dragonestia.picker.api.model.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IUser {

    @NotNull String getIdentifier();

    @Nullable String getDetail(@NotNull UserDetails detail);
}
