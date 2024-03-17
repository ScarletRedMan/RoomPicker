package ru.dragonestia.picker.api.impl.exception;

public class NotEnoughPermissions extends RuntimeException {

    public NotEnoughPermissions(String message) {
        super(message);
    }
}
