package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RoomRepository {

    List<RoomId> allRoomsIds(InstanceId instanceId);

    Room getRoom(InstanceId instanceId, RoomId roomId);

    Map<RoomId, Room> getRooms(InstanceId instanceId, Collection<RoomId> rooms);

    void createRoom(InstanceId instanceId, RoomId roomId, int slots, String payload, boolean locked, boolean persist);

    void deleteRoom(InstanceId instanceId, RoomId roomId);

    default void deleteRoom(Room room) {
        deleteRoom(room.instanceId(), room.id());
    }

    void deleteRooms(InstanceId instanceId, Collection<RoomId> rooms);

    default void deleteRooms(Collection<Room> rooms) {
        InstanceId targetInstance = null;
        List<RoomId> toDelete = new ArrayList<>();
        List<Room> undeleted = new ArrayList<>();
        for (var room: rooms) {
            if (targetInstance == null) {
                targetInstance = room.instanceId();
            } else if (!targetInstance.equals(room.instanceId())) {
                undeleted.add(room);
                continue;
            }

            toDelete.add(room.id());
        }

        deleteRooms(targetInstance, toDelete);

        if (!undeleted.isEmpty()) {
            deleteRooms(undeleted);
        }
    }

    void lockRoom(InstanceId instanceId, RoomId roomId, boolean newState);

    default void lockRoom(Room room, boolean newState) {
        lockRoom(room.instanceId(), room.id(), newState);
    }
}
