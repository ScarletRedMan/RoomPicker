package ru.dragonestia.picker.api.model.room;

import ru.dragonestia.picker.api.model.instance.InstanceId;

public record Room(RoomId id, InstanceId instanceId, int slots, boolean locked, String payload, boolean persist) {}
