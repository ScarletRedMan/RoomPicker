package ru.dragonestia.picker.api.exception;

public final class InvalidNodeIdentifierException extends ApiException {

    public static final String ERROR_ID = "err.node.invalid_identifier";

    private final String nodeId;

    public InvalidNodeIdentifierException(String nodeId) {
        this.nodeId = nodeId;
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
}
