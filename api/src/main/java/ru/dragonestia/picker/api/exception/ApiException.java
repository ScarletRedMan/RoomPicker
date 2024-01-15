package ru.dragonestia.picker.api.exception;

public abstract class ApiException extends RuntimeException {

    public abstract String getErrorId();
}
