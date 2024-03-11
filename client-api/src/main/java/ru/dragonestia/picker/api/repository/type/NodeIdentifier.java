package ru.dragonestia.picker.api.repository.type;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.util.IdentifierValidator;

import java.security.InvalidParameterException;

public final class NodeIdentifier extends ValueObject<String> {

    private NodeIdentifier(String value) {
        super(value);
    }

    @Override
    protected void validate(String value) {
        if (IdentifierValidator.forNode(value)) return;

        throw new InvalidParameterException("Invalid node identifier");
    }

    public static NodeIdentifier of(@NotNull String value) {
        return new NodeIdentifier(value);
    }
}
