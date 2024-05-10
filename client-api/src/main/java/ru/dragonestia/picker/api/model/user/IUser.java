package ru.dragonestia.picker.api.model.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;

import java.beans.Transient;

public interface IUser {

    @NotNull String getIdentifier();

    @Transient
    default @NotNull EntityIdentifier getIdentifierObject() {
        return EntityIdentifier.of(getIdentifier());
    }

    @Nullable String getDetail(@NotNull UserDetails detail);
}
