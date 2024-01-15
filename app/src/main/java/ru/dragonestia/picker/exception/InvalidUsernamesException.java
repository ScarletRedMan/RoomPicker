package ru.dragonestia.picker.exception;

import lombok.Getter;

import java.util.List;

@Getter
public final class InvalidUsernamesException extends RuntimeException {

    private final List<String> givenUsernames;
    private final List<String> usernamesWithErrors;

    public InvalidUsernamesException(List<String> givenUsernames, List<String> usernamesWithErrors) {
        this.givenUsernames = givenUsernames;
        this.usernamesWithErrors = usernamesWithErrors;
    }

    @Override
    public String getMessage() {
        return "Users with invalid identifiers were found";
    }
}
