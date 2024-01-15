package ru.dragonestia.picker.exception;

import lombok.Getter;

@Getter
public final class RoomAreFullException extends RuntimeException {

    private final String nodeId;
    private final String roomId;

    public RoomAreFullException(String nodeId, String roomId) {
        this.nodeId = nodeId;
        this.roomId = roomId;
    }

    @Override
    public String getMessage() {
        return "Room with identifier '" + roomId + "' in node '" + nodeId + "' are full";
    }
}
