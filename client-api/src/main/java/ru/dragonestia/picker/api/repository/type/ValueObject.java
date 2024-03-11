package ru.dragonestia.picker.api.repository.type;

abstract class ValueObject<T> {

    private final T value;

    ValueObject(T value) {
        validate(value);

        this.value = value;
    }

    protected abstract void validate(T value);

    public T getValue() {
        return value;
    }
}
