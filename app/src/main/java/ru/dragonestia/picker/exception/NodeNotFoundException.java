package ru.dragonestia.picker.exception;

import lombok.Getter;

@Getter
public final class NodeNotFoundException extends RuntimeException {

    private final String nodeId;

    public NodeNotFoundException(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getMessage() {
        return "Node '" + nodeId + "' does not found";
    }
}
