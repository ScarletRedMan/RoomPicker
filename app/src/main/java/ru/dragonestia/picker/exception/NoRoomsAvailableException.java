package ru.dragonestia.picker.exception;

import lombok.Getter;

@Getter
public class NoRoomsAvailableException extends RuntimeException {

    private final String nodeId;

    public NoRoomsAvailableException(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getMessage() {
        return "There are no rooms available in node '" + nodeId + "'";
    }
}
