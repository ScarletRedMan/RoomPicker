package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.NodeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class NodeRepositoryImpl implements NodeRepository {

    private final RoomRepository roomRepository;
    private final Map<String, Node> nodeMap = new ConcurrentHashMap<>();

    @Override
    public void create(Node node) {
        synchronized (nodeMap) {
            if (nodeMap.containsKey(node.id())) {
                throw new IllegalArgumentException("Node with id '" + node.id() + "' already exists");
            }

            nodeMap.put(node.id(), node);
        }

        roomRepository.onCreateNode(node);
    }

    @Override
    public void delete(Node node) {
        synchronized (nodeMap) {
            nodeMap.remove(node.id());
        }

        roomRepository.onRemoveNode(node);
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
