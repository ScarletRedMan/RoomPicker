package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class InvalidRoomIdentifierException extends ApiException {

    public static final String ERROR_ID = "err.room.invalid_identifier";

    private final String nodeId;
    private final String roomId;

    public InvalidRoomIdentifierException(String nodeId, String roomId) {
        this.nodeId = nodeId;
        this.roomId = roomId;
    }

    public InvalidRoomIdentifierException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("nodeId"), errorResponse.details().get("roomId"));
    }

    @Override
    public String getMessage() {
        return "Invalid room identifier. Taken '" + roomId + "'";
    }

    public String getRoomId() {
        return roomId;
    }

    public String getNodeId() {
        return nodeId;
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
