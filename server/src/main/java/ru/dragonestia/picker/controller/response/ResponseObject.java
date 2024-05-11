package ru.dragonestia.picker.controller.response;

import ru.dragonestia.picker.model.instance.type.PickingMethod;

import java.util.List;

public final class ResponseObject {

    private ResponseObject() {}

    public record Instance(String id, PickingMethod method, boolean persist) {

        public static ResponseObject.Instance of(ru.dragonestia.picker.model.instance.Instance instance) {
            return new Instance(instance.getId().getValue(), instance.getPickingMethod(), instance.isPersist());
        }
    }

    public record Room(String id, String nodeId, int slots, boolean locked, boolean persist, String payload) {

        public static ResponseObject.Room of(ru.dragonestia.picker.model.room.Room room) {
            return new Room(room.getId().getValue(),
                    room.getInstance().getId().getValue(),
                    room.getSlots(),
                    room.isLocked(),
                    room.isPersist(),
                    room.getPayload());
        }
    }

    public record PickedRoom(Room room, List<String> entities) {}

    public record Account(String id, List<String> permissions, boolean locked) {

        public static Account of(ru.dragonestia.picker.model.account.Account account) {
            return new Account(account.getUsername(), account.getAuthorities().stream().map(Enum::name).toList(), account.isLocked());
        }
    }
}
