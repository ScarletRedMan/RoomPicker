package ru.dragonestia.picker.api.impl.exception;

import ru.dragonestia.picker.api.exception.ApiException;

@ApiException
public class GraphqlException extends RuntimeException {

    public GraphqlException(String message) {
        super(message);
    }
}
