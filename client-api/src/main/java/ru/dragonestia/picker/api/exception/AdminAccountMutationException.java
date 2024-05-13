package ru.dragonestia.picker.api.exception;

@ApiException
public class AdminAccountMutationException extends RuntimeException {

    public AdminAccountMutationException(String message) {
        super(message);
    }
}
