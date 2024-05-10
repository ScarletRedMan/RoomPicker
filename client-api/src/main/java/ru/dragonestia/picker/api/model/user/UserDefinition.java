package ru.dragonestia.picker.api.model.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;

public class UserDefinition implements IUser {

    private final String id;

    public UserDefinition(@NotNull EntityIdentifier identifier) {
        id = identifier.getValue();
    }

    @Override
    public @NotNull String getIdentifier() {
        return id;
    }

    @Override
    public @Nullable String getDetail(@NotNull UserDetails detail) {
        throw new UnsupportedOperationException();
    }
}
