package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class NotPersistedNodeException extends ApiException {

    public final static String ERROR_ID = "rr.room.node_not_persisted";

    private final String nodeId;
    private final String roomId;

    public NotPersistedNodeException(String nodeId, String roomId) {
        this.nodeId = nodeId;
        this.roomId = roomId;
    }

    public NotPersistedNodeException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("node"), errorResponse.details().get("room"));
    }

    @Override
    public String getMessage() {
        return "Cannot create persist room '%s' in non persist node '%s'".formatted(getRoomId(), getNodeId());
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
