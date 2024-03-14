package ru.dragonestia.picker.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

public class User implements IUser {

    private final String identifier;

    public User(@NotNull UserIdentifier identifier) {
        this.identifier = identifier.getValue();
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @Nullable String getDetail(@NotNull UserDetails detail) {
        throw new UnsupportedOperationException();
    }

    public @NotNull ResponseUser toResponseObject() {
        return new ResponseUser(identifier);
    }

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof User other) {
            return identifier.equals(other.identifier);
        }
        return false;
    }
}
