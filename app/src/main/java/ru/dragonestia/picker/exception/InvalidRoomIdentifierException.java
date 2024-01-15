package ru.dragonestia.picker.exception;

import lombok.Getter;

@Getter
public final class InvalidRoomIdentifierException extends RuntimeException {

    private final String roomId;

    public InvalidRoomIdentifierException(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String getMessage() {
        return "Invalid room identifier. Taken '" + roomId + "'";
    }
}
