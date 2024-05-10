package ru.dragonestia.picker.api.repository.type;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.util.IdentifierValidator;

import java.security.InvalidParameterException;

public class EntityIdentifier extends ValueObject<String> {

    private EntityIdentifier(String value) {
        super(value);
    }

    @Override
    protected void validate(String value) {
        if (IdentifierValidator.forUser(value)) return;

        throw new InvalidParameterException("Invalid user identifier");
    }

    public static @NotNull EntityIdentifier of(@NotNull String identifier) {
        return new EntityIdentifier(identifier);
    }
}
