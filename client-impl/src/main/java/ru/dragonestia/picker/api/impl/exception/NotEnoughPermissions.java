package ru.dragonestia.picker.api.impl.exception;

import ru.dragonestia.picker.api.exception.ApiException;

@ApiException
public class NotEnoughPermissions extends RuntimeException {

    public NotEnoughPermissions(String message) {
        super(message);
    }
}
