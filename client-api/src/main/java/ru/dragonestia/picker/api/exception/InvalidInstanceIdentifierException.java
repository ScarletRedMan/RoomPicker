package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class InvalidInstanceIdentifierException extends ApiException {

    public static final String ERROR_ID = "err.node.invalid_identifier";

    private final String nodeId;

    public InvalidInstanceIdentifierException(String nodeId) {
        this.nodeId = nodeId;
    }

    public InvalidInstanceIdentifierException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("identifier"));
    }

    @Override
    public String getMessage() {
        return "Invalid node identifier. Taken '" + nodeId + "'";
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
        details.put("identifier", getNodeId());
    }
}
