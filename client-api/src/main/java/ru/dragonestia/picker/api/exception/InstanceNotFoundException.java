package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class InstanceNotFoundException extends ApiException {

    public static final String ERROR_ID = "err.instance.not_found";

    private final String nodeId;

    public InstanceNotFoundException(String nodeId) {
        this.nodeId = nodeId;
    }

    public InstanceNotFoundException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("instance"));
    }

    @Override
    public String getMessage() {
        return "Instance '" + nodeId + "' does not found";
    }

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public String getErrorId() {
        return ERROR_ID;
    }

    @Override
    public void appendDetailsToErrorResponse(Map<String, String> details) {
        details.put("instance", getNodeId());
    }
}
