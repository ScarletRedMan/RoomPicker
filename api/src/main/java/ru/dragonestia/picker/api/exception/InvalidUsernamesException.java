package ru.dragonestia.picker.api.exception;

import java.util.List;

public final class InvalidUsernamesException extends ApiException {

    public static final String ERROR_ID = "err.user.invalid_identifier";

    private final List<String> givenUsernames;
    private final List<String> invalidUsernames;

    public InvalidUsernamesException(List<String> givenUsernames, List<String> invalidUsernames) {
        this.givenUsernames = givenUsernames;
        this.invalidUsernames = invalidUsernames;
    }

    @Override
    public String getMessage() {
        return "Users with invalid identifiers were found";
    }

    public List<String> getGivenUsernames() {
        return givenUsernames;
    }

    public List<String> getInvalidUsernames() {
        return invalidUsernames;
    }

    @Override
    public String getErrorId() {
        return ERROR_ID;
    }
}
