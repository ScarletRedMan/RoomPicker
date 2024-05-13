package ru.dragonestia.picker.api.exception;

@ApiException
public class ConflictingPersistParametersException extends RuntimeException {

    public ConflictingPersistParametersException(String message) {
        super(message);
    }
}
