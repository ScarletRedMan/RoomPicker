package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public class AccountDoesNotExistsException extends ApiException {

    public static final String ERROR_ID = "err.account.does_not_exists";

    private final String accountId;

    public AccountDoesNotExistsException(String accountId) {
        this.accountId = accountId;
    }

    public AccountDoesNotExistsException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("accountId"));
    }

    public String getAccountId() {
        return accountId;
    }

    @Override
    public String getErrorId() {
        return ERROR_ID;
    }

    @Override
    public String getMessage() {
        return "Account with id '" + accountId + "' does not exists";
    }

    @Override
    public void appendDetailsToErrorResponse(Map<String, String> details) {
        details.put("accountId", accountId);
    }
}
