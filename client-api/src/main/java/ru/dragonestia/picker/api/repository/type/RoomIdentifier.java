package ru.dragonestia.picker.api.repository.type;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.util.IdentifierValidator;

import java.security.InvalidParameterException;

public class RoomIdentifier extends ValueObject<String> {

    private RoomIdentifier(String value) {
        super(value);
    }

    @Override
    protected void validate(String value) {
        if(IdentifierValidator.forRoom(value)) return;

        throw new InvalidParameterException("Invalid room identifier");
    }

    public static RoomIdentifier of(@NotNull String value) {
        return new RoomIdentifier(value);
    }
}
