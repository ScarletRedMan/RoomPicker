package ru.dragonestia.picker.exception;

import lombok.Getter;

@Getter
public final class InvalidNodeIdentifierException extends RuntimeException {

    private final String nodeId;

    public InvalidNodeIdentifierException(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getMessage() {
        return "Invalid node identifier. Taken '" + nodeId + "'";
    }
}
