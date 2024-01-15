package ru.dragonestia.picker.api.exception;

public final class NodeNotFoundException extends ApiException {

    public static final String ERROR_ID = "err.node.not_found";

    private final String nodeId;

    public NodeNotFoundException(String nodeId) {
        this.nodeId = nodeId;
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
}
