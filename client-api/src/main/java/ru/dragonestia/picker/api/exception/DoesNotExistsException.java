package ru.dragonestia.picker.api.exception;

@ApiException
public class DoesNotExistsException extends RuntimeException {

    public DoesNotExistsException(String message) {
        super(message);
    }
}
