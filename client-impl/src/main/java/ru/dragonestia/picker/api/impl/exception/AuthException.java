package ru.dragonestia.picker.api.impl.exception;

import ru.dragonestia.picker.api.exception.ApiException;

@ApiException
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
