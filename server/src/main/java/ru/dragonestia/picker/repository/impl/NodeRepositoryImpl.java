package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.impl.cache.NodeId2PickerModeCache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class NodeRepositoryImpl implements NodeRepository {

    private final RoomRepository roomRepository;
    private final PickerRepository pickerRepository;
    private final NodeId2PickerModeCache nodeId2PickerModeCache;
    private final Map<String, Node> nodeMap = new ConcurrentHashMap<>();

    @Override
    public void create(Node node) throws NodeAlreadyExistException {
        synchronized (nodeMap) {
            if (nodeMap.containsKey(node.getIdentifier())) {
                throw new NodeAlreadyExistException(node.getIdentifier());
            }

            nodeMap.put(node.getIdentifier(), node);
            var picker = pickerRepository.create(node.getIdentifier(), node.getPickingMethod());
            nodeId2PickerModeCache.put(node.getIdentifier(), picker);
        }

        roomRepository.onCreateNode(node);
    }

    @Override
    public List<Room> delete(Node node) {
        synchronized (nodeMap) {
            nodeMap.remove(node.getIdentifier());
            pickerRepository.remove(node.getIdentifier());
            nodeId2PickerModeCache.remove(node.getIdentifier());
        }

        return roomRepository.onRemoveNode(node);
    }

    @Override
    public Optional<Node> find(String nodeId) {
        synchronized (nodeMap) {
            return nodeMap.containsKey(nodeId)? Optional.of(nodeMap.get(nodeId)) : Optional.empty();
        }
    }

    @Override
    public List<Node> all() {
        synchronized (nodeMap) {
            return nodeMap.values().stream().toList();
        }
    }
}
