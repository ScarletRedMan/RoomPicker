package ru.dragonestia.picker.api.impl.exception;

import ru.dragonestia.picker.api.exception.ApiException;

@ApiException
public class UnknownException extends RuntimeException {

    public UnknownException(String message) {
        super(message);
    }
}
