package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.Collection;
import java.util.List;

public interface EntityService {

    Collection<Room> getEntityRooms(Entity entity);

    void linkEntitiesWithRoom(Room room, Collection<Entity> entities, boolean force) throws RoomAreFullException;

    void unlinkEntitiesFromRoom(Room room, Collection<Entity> entities);

    Collection<Entity> getRoomEntities(Room room);

    List<ResponseUser> searchEntities(String input);

    ResponseUser getEntityDetails(String userId);
}
