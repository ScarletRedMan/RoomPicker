package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.repository.EntityRepository;
import ru.dragonestia.picker.service.EntityService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class EntityServiceImpl implements EntityService {

    private final EntityRepository entityRepository;

    @Override
    public Collection<Room> getEntityRooms(Entity entity) {
        return entityRepository.findAllLinkedEntityRooms(entity);
    }

    @Override
    public void linkEntitiesWithRoom(Room room, Collection<Entity> entities, boolean force) {
        entityRepository.linkWithRoom(room, entities, force);
    }

    @Override
    public void unlinkEntitiesFromRoom(Room room, Collection<Entity> entities) {
        entityRepository.unlinkWithRoom(room, entities);
    }

    @Override
    public Collection<Entity> getRoomEntities(Room room) {
        return entityRepository.entitiesOf(room);
    }

    @Override
    public List<ResponseUser> searchEntities(String input) {
        return entityRepository.search(input).stream().map(Entity::toResponseObject).toList();
    }

    @Override
    public ResponseUser getEntityDetails(String userId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
