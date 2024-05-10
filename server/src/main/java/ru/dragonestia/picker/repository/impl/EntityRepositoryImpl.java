package ru.dragonestia.picker.repository.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.InstanceNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.repository.EntityRepository;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class EntityRepositoryImpl implements EntityRepository {

    private final ContainerRepository containerRepository;

    private final Map<Entity, Set<Room>> entityRooms = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        containerRepository.setTransactionListener(transaction -> {
            synchronized (entityRooms) {
                for (var entity: transaction.target()) {
                    var set = entityRooms.computeIfAbsent(entity, k -> new HashSet<>());
                    set.add(transaction.room());
                }
            }
        });
    }

    @Override
    public void linkWithRoom(Room room, Collection<Entity> entities, boolean force) throws RoomAreFullException {
        synchronized (entityRooms) {
            getRoomContainer(room).addEntities(entities, force);

            for (var entity: entities) {
                var set = entityRooms.computeIfAbsent(entity, k -> new HashSet<>());
                set.add(room);
            }
        }
    }

    @Override
    public void unlinkWithRoom(Room room, Collection<Entity> entities) {
        synchronized (entityRooms) {
            getRoomContainer(room).removeEntities(entities);

            for (var entity: entities) {
                var set = entityRooms.get(entity);
                if (set == null) continue;
                set.remove(room);
                if (set.isEmpty()) entityRooms.remove(entity);
            }
        }
    }

    @Override
    public Collection<Room> findAllLinkedEntityRooms(Entity entity) {
        var result = entityRooms.get(entity);
        return Collections.unmodifiableSet(result == null? new HashSet<>() : result);
    }

    @Override
    public Collection<Entity> entitiesOf(Room room) {
        return getRoomContainer(room).allEntities();
    }

    @Override
    public Collection<Entity> search(String input) {
        return entityRooms.keySet().stream().filter(entity -> entity.getIdentifier().startsWith(input)).toList();
    }

    @Override
    public int countAllEntities() {
        return entityRooms.size();
    }

    @Override
    public Map<String, Integer> countEntitiesForNodes() {
        var result = new HashMap<String, Integer>();

        containerRepository.all().forEach(nodeContainer -> {
            var nodeId = nodeContainer.getInstance().getIdentifier();

            nodeContainer.allRooms().forEach(roomContainer -> {
                result.put(nodeId, result.getOrDefault(nodeId, 0) + roomContainer.countEntities());
            });
        });

        return result;
    }

    private RoomContainer getRoomContainer(Room room) {
        return containerRepository.findById(room.getInstanceIdentifier())
                .orElseThrow(() -> new InstanceNotFoundException(room.getInstanceIdentifier()))
                .findRoomById(room.getIdentifier())
                .orElseThrow(() -> new RoomNotFoundException(room.getInstanceIdentifier(), room.getIdentifier()));
    }
}
