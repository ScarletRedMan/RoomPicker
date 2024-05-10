package ru.dragonestia.picker.exception;

import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.RoomId;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public static AlreadyExistsException forInstance(InstanceId instanceId) {
        return new AlreadyExistsException("Instance with id '%s' already created".formatted(instanceId.getValue()));
    }

    public static AlreadyExistsException forRoom(InstanceId instanceId, RoomId roomId) {
        return new AlreadyExistsException("Room with id '%s' already exists in instance '%s".formatted(roomId.getValue(), instanceId.getValue()));
    }
}
