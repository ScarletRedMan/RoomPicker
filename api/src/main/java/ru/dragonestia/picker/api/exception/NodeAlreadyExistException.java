package ru.dragonestia.picker.api.exception;

public final class NodeAlreadyExistException extends ApiException {

    public static final String ERROR_ID = "err.node.already_exists";

    private final String nodeId;

    public NodeAlreadyExistException(String nodeId) {
        this.nodeId = nodeId;
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
}
