package ru.dragonestia.picker.api.exception;

import java.util.Map;

public class ConstantAdminParamsException extends ApiException {

    public static final String ERROR_ID = "err.account.admin.modification";

    @Override
    public String getErrorId() {
        return ERROR_ID;
    }

    @Override
    public void appendDetailsToErrorResponse(Map<String, String> details) {}
}
