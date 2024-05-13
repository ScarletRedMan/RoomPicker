package ru.dragonestia.picker.api.exception;

import org.jetbrains.annotations.Nullable;

@ApiException
public class InvalidIdentifierException extends RuntimeException {

    public InvalidIdentifierException(String message) {
        super(message);
    }

    public static InvalidIdentifierException taken(@Nullable String input) {
        return new InvalidIdentifierException("Taken identifier: " + input);
    }
}
