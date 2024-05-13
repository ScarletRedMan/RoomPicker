package ru.dragonestia.picker.api.exception;

@ApiException
public class RoomAreFullException extends RuntimeException {

    public RoomAreFullException(String message) {
        super(message);
    }
}
