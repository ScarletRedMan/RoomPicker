package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class RoomAlreadyExistException extends ApiException {

    public static final String ERROR_ID = "err.room.already_exists";

    private final String nodeId;
    private final String roomId;

    public RoomAlreadyExistException(String nodeId, String roomId) {
        this.nodeId = nodeId;
        this.roomId = roomId;
    }

    public RoomAlreadyExistException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("nodeId"),
                errorResponse.details().get("roomId"));
    }

    @Override
    public String getMessage() {
        return "Room with identifier '" + roomId + "' in node '" + nodeId + "' is already exists";
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
        details.put("nodeId", getNodeId());
        details.put("roomId", getRoomId());
    }
}
