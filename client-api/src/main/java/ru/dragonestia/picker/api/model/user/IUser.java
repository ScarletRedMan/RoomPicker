package ru.dragonestia.picker.api.model.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

public interface IUser {

    @NotNull String getIdentifier();

    default @NotNull UserIdentifier getIdentifierObject() {
        return UserIdentifier.of(getIdentifier());
    }

    @Nullable String getDetail(@NotNull UserDetails detail);
}
