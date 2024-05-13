package ru.dragonestia.picker.api.model.instance;

import ru.dragonestia.picker.api.exception.InvalidIdentifierException;

import java.util.Objects;
import java.util.Random;

public final class InstanceId {

    private static final Random random = new Random();

    private final String value;

    private InstanceId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceId that = (InstanceId) o;
        return Objects.equals(value, that.value);
    }

    public static InstanceId of(String identifier) throws InvalidIdentifierException {
        if (identifier.matches("^(?!-)[a-z\\d-]{0,31}[a-z\\d](?!-)$")) {
            return new InstanceId(identifier);
        }

        throw InvalidIdentifierException.taken(identifier);
    }

    public static InstanceId random() {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('a' + random.nextInt('z' - 'a'));
        }
        return new InstanceId(new String(chars));
    }
}
