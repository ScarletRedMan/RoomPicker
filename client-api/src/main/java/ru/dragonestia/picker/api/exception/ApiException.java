package ru.dragonestia.picker.api.exception;

import java.util.Map;

public abstract class ApiException extends RuntimeException {

    public abstract String getErrorId();

    public abstract void appendDetailsToErrorResponse(Map<String, String> details);
}
