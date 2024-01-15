package ru.dragonestia.picker.api.exception;

public final class InvalidRoomIdentifierException extends ApiException {

    public static final String ERROR_ID = "err.room.invalid_identifier";

    private final String roomId;

    public InvalidRoomIdentifierException(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String getMessage() {
        return "Invalid room identifier. Taken '" + roomId + "'";
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String getErrorId() {
        return ERROR_ID;
    }
}
