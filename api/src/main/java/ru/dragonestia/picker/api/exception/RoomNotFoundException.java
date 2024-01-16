package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class RoomNotFoundException extends ApiException {

    public static final String ERROR_ID = "err.room.not_found";

    private final String nodeId;
    private final String roomId;

    public RoomNotFoundException(String nodeId, String roomId) {
        this.nodeId = nodeId;
        this.roomId = roomId;
    }

    public RoomNotFoundException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("nodeId"),
                errorResponse.details().get("roomId"));
    }

    @Override
    public String getMessage() {
        return "Room '" + roomId + "' in node '" + nodeId + "' does not found";
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String getErrorId() {
        return ERROR_ID;
    }

    @Override
    public void appendDetailsToErrorResponse(Map<String, String> details) {
        details.put("node", getNodeId());
        details.put("room", getRoomId());
    }
}
