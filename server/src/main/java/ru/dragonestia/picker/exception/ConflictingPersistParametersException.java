package ru.dragonestia.picker.exception;

import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.RoomId;

public class ConflictingPersistParametersException extends RuntimeException {

    public ConflictingPersistParametersException(String message) {
        super(message);
    }

    public static ConflictingPersistParametersException forRoom(InstanceId instanceId, RoomId roomId) {
        return new ConflictingPersistParametersException("Tried create persisted room '%s' for not persisted instance '%s'"
                .formatted(
                        roomId.getValue(),
                        instanceId.getValue()
                ));
    }
}
