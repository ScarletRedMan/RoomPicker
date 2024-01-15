package ru.dragonestia.picker.api.exception;

public class NoRoomsAvailableException extends ApiException {

    public static final String ERROR_ID = "err.room.no_available";

    private final String nodeId;

    public NoRoomsAvailableException(String nodeId) {
        this.nodeId = nodeId;
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
}
