package ru.dragonestia.picker.exception;

import lombok.Getter;

@Getter
public final class RoomNotFoundException extends RuntimeException {

    private final String nodeId;
    private final String roomId;

    public RoomNotFoundException(String nodeId, String roomId) {
        this.nodeId = nodeId;
        this.roomId = roomId;
    }

    @Override
    public String getMessage() {
        return "Room '" + roomId + "' in node '" + nodeId + "' does not found";
    }
}
