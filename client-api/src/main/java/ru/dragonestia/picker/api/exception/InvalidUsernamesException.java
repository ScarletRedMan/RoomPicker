package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.*;
import java.util.function.Function;

public final class InvalidUsernamesException extends ApiException {

    public static final String ERROR_ID = "err.user.invalid_identifier";

    private final List<String> givenUsernames;
    private final List<String> invalidUsernames;

    public InvalidUsernamesException(List<String> givenUsernames, List<String> invalidUsernames) {
        this.givenUsernames = givenUsernames;
        this.invalidUsernames = invalidUsernames;
    }

    public InvalidUsernamesException(ErrorResponse errorResponse) {
        this(Arrays.stream(errorResponse.details().get("givenUsernames").split(",")).toList(),
                Arrays.stream(errorResponse.details().get("invalidUsernames").split(",")).toList());
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

    @Override
    public void appendDetailsToErrorResponse(Map<String, String> details) {
        Function<Collection<String>, String> toString = input -> String.join(",", input);

        details.put("givenUsernames", toString.apply(getGivenUsernames()));
        details.put("invalidUsernames", toString.apply(getInvalidUsernames()));
    }
}
