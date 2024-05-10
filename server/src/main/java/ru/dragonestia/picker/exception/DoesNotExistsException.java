package ru.dragonestia.picker.exception;

import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.RoomId;

public class DoesNotExistsException extends RuntimeException {

    public DoesNotExistsException(String message) {
        super(message);
    }

    public static DoesNotExistsException forInstance(InstanceId id) {
        return new DoesNotExistsException("Does not exists instance with id '%s'".formatted(id.toString()));
    }

    public static DoesNotExistsException forRoom(RoomId id) {
        return new DoesNotExistsException("Does not exists room with id '%s'".formatted(id.toString()));
    }
}
