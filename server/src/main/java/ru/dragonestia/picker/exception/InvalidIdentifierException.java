package ru.dragonestia.picker.exception;

import org.jetbrains.annotations.Nullable;

public class InvalidIdentifierException extends RuntimeException {

    public InvalidIdentifierException(String message) {
        super(message);
    }

    public static InvalidIdentifierException taken(@Nullable String input) {
        return new InvalidIdentifierException("Taken identifier: " + input);
    }
}
