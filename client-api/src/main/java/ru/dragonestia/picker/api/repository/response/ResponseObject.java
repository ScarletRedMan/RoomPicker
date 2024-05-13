package ru.dragonestia.picker.api.repository.response;

import ru.dragonestia.picker.api.model.account.Account;
import ru.dragonestia.picker.api.model.account.AccountId;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.Instance;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.instance.type.PickingMethod;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;

import java.util.List;

public final class ResponseObject {

    private ResponseObject() {}

    public static class RInstance {
        private String id;
        private PickingMethod method;
        private boolean persist;

        public Instance convert() {
            return new Instance(InstanceId.of(id), method, persist);
        }
    }

    public static class RRoom {
        private String id;
        private String instanceId;
        private int slots;
        private boolean locked;
        private boolean persist;
        private String payload;

        public Room covert() {
            return new Room(RoomId.of(id), InstanceId.of(instanceId), slots, locked, payload, persist);
        }
    }

    public static class PickedRoom {
        private RRoom room;
        private List<String> entities;

        private transient Room cachedRoom;
        private transient List<EntityId> cachedEntities;

        public Room getRoom() {
            if (cachedRoom == null) cachedRoom = room.covert();
            return cachedRoom;
        }

        public List<EntityId> getEntities() {
            if (cachedEntities == null) cachedEntities = entities.stream().map(EntityId::of).toList();
            return cachedEntities;
        }
    }

    public static class RAccount {
        private String id;
        private List<String> permissions;
        private boolean locked;

        public Account convert() {
            return new Account(AccountId.of(id), permissions.stream().map(Permission::valueOf).toList(), locked);
        }
    }
}
