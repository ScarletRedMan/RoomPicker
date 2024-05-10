package ru.dragonestia.picker.controller.response;

import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.type.PickingMethod;

import java.util.List;

public final class ResponseObject {

    private ResponseObject() {}

    public record Instance(String id, PickingMethod method, boolean persist) {}

    public record Room(String id, String nodeId, int slots, boolean locked, boolean persist, String payload) {}

    public record PickedRoom(Room room, List<EntityId> entities) {}

    public record Account(String id, List<String> permissions, boolean locked) {}
}
