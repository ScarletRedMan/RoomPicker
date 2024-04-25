package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class PermissionNotFoundException extends ApiException {

    public static final String ERROR_ID = "err.permission.not_found";

    private final String permissionId;

    public PermissionNotFoundException(String permissionId) {
        this.permissionId = permissionId;
    }

    public PermissionNotFoundException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("permissionId"));
    }

    public String getPermissionId() {
        return permissionId;
    }

    @Override
    public String getErrorId() {
        return ERROR_ID;
    }

    @Override
    public void appendDetailsToErrorResponse(Map<String, String> details) {
        details.put("permissionId", permissionId);
    }
}
