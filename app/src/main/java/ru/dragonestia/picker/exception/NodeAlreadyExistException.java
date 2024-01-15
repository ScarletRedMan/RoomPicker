package ru.dragonestia.picker.exception;

import lombok.Getter;

@Getter
public final class NodeAlreadyExistException extends RuntimeException {

    private final String nodeId;

    public NodeAlreadyExistException(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getMessage() {
        return "Node with identifier '" + nodeId + "' is already exists";
    }
}
