package ru.dragonestia.picker.exception;

import ru.dragonestia.picker.model.instance.InstanceId;

public class NoRoomsAvailableException extends RuntimeException {

    public NoRoomsAvailableException(InstanceId instanceId) {
        super("There are no rooms available in instance '" + instanceId.getValue() + "'");
    }
}
