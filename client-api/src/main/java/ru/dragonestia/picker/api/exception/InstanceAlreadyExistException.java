package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class InstanceAlreadyExistException extends ApiException {

    public static final String ERROR_ID = "err.node.already_exists";

    private final String nodeId;

    public InstanceAlreadyExistException(String nodeId) {
        this.nodeId = nodeId;
    }

    public InstanceAlreadyExistException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("nodeId"));
    }

    @Override
    public String getMessage() {
        return "Node with identifier '" + nodeId + "' is already exists";
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
        details.put("nodeId", getNodeId());
    }
}
