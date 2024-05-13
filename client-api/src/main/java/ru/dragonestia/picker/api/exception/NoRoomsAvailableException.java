package ru.dragonestia.picker.api.exception;

@ApiException
public class NoRoomsAvailableException extends RuntimeException {

    public NoRoomsAvailableException(String message) {
        super(message);
    }
}
