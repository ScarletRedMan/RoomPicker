package ru.dragonestia.picker.exception;

public class AdminAccountMutationException extends RuntimeException {

    public AdminAccountMutationException() {
        super("Cannot mutate admin account");
    }
}
