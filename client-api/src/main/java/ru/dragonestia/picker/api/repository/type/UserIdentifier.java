package ru.dragonestia.picker.api.repository.type;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.util.IdentifierValidator;

import java.security.InvalidParameterException;

public class UserIdentifier extends ValueObject<String> {

    private UserIdentifier(String value) {
        super(value);
    }

    @Override
    protected void validate(String value) {
        if (IdentifierValidator.forUser(value)) return;

        throw new InvalidParameterException("Invalid user identifier");
    }

    public static @NotNull UserIdentifier of(@NotNull String identifier) {
        return new UserIdentifier(identifier);
    }
}
