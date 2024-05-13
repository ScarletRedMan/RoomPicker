package ru.dragonestia.picker.api.exception;

@ApiException
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }
}
