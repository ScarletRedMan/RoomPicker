package ru.dragonestia.loadbalancer.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.repository.BucketRepository;
import ru.dragonestia.loadbalancer.repository.NodeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class NodeRepositoryImpl implements NodeRepository {

    private final BucketRepository bucketRepository;
    private final Map<String, Node> nodeMap = new ConcurrentHashMap<>();

    @Override
    public void createNode(Node node) {
        synchronized (nodeMap) {
            if (nodeMap.containsKey(node.identifier())) {
                throw new IllegalArgumentException("Node with id '" + node.identifier() + "' already exists");
            }

            nodeMap.put(node.identifier(), node);
        }

        bucketRepository.onCreateNode(node);
    }

    @Override
    public void deleteNode(Node node) {
        synchronized (nodeMap) {
            nodeMap.remove(node.identifier());
        }

        bucketRepository.onRemoveNode(node);
    }

    @Override
    public Optional<Node> findNode(String nodeId) {
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
