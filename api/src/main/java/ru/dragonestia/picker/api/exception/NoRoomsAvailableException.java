package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.Map;

public final class NoRoomsAvailableException extends ApiException {

    public static final String ERROR_ID = "err.room.no_available";

    private final String nodeId;

    public NoRoomsAvailableException(String nodeId) {
        this.nodeId = nodeId;
    }

    public NoRoomsAvailableException(ErrorResponse errorResponse) {
        this(errorResponse.details().get("nodeId"));
    }

    @Override
    public String getMessage() {
        return "There are no rooms available in node '" + nodeId + "'";
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
    }
}
