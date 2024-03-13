package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.impl.cache.NodeId2PickerModeCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@RequiredArgsConstructor
public class NodeRepositoryImpl implements NodeRepository {

    private final RoomRepository roomRepository;
    private final PickerRepository pickerRepository;
    private final NodeId2PickerModeCache nodeId2PickerModeCache;
    private final Map<String, Node> nodeMap = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void create(Node node) throws NodeAlreadyExistException {
        lock.writeLock().lock();
        try {
            if (nodeMap.containsKey(node.getIdentifier())) {
                throw new NodeAlreadyExistException(node.getIdentifier());
            }

            nodeMap.put(node.getIdentifier(), node);
            var picker = pickerRepository.create(node.getIdentifier(), node.getPickingMethod());
            nodeId2PickerModeCache.put(node.getIdentifier(), picker);

            roomRepository.onCreateNode(node);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Room> delete(Node node) {
        lock.writeLock().lock();
        try {
            nodeMap.remove(node.getIdentifier());
            pickerRepository.remove(node.getIdentifier());
            nodeId2PickerModeCache.remove(node.getIdentifier());

            return roomRepository.onRemoveNode(node);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Node> find(String nodeId) {
        lock.readLock().lock();
        try {
            return nodeMap.containsKey(nodeId)? Optional.of(nodeMap.get(nodeId)) : Optional.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Node> all() {
        lock.readLock().lock();
        try {
            return nodeMap.values().stream().toList();
        } finally {
            lock.readLock().unlock();
        }
    }
}
