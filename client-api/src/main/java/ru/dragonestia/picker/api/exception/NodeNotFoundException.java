package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class NodeNotFoundException extends ApiException {

    public static final String ERROR_ID = "err.node.not_found";

    private final String nodeId;

    public NodeNotFoundException(String nodeId) {
        this.nodeId = nodeId;
    }

    public NodeNotFoundException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("node"));
    }

    @Override
    public String getMessage() {
        return "Node '" + nodeId + "' does not found";
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
        details.put("node", getNodeId());
    }
}
