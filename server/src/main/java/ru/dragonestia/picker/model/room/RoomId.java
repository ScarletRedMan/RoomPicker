package ru.dragonestia.picker.model.room;

import lombok.Getter;
import ru.dragonestia.picker.exception.InvalidIdentifierException;
import ru.dragonestia.picker.model.instance.InstanceId;

import java.util.Objects;
import java.util.Random;

@Getter
public final class RoomId {

    private final static Random random = new Random();

    private final String value;

    private RoomId(String value) {
        this.value = value;
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
        RoomId roomId = (RoomId) o;
        return Objects.equals(value, roomId.value);
    }

    public static RoomId of(String identifier) throws InvalidIdentifierException {
        if (identifier.matches("^(?!-)[a-z\\d-]{0,31}[a-z\\d](?!-)$")) {
            return new RoomId(identifier);
        }

        throw InvalidIdentifierException.taken(identifier);
    }

    public static RoomId random() {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ('a' + random.nextInt('z' - 'a'));
        }
        return new RoomId(new String(chars));
    }
}
