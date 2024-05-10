package ru.dragonestia.picker.exception;

import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.RoomId;

public class RoomAreFullException extends RuntimeException {

    public RoomAreFullException(InstanceId instanceId, RoomId roomId) {
        super("Room '%s' in instance '%s' are full".formatted(roomId.getValue(), instanceId.toString()));
    }
}
