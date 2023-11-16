package ru.dragonestia.loadbalancer.service.impl;

import org.springframework.stereotype.Service;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.service.NodeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class NodeServiceImpl implements NodeService {

    private final Map<String, Node> nodes = new ConcurrentHashMap<>();

    @Override
    public void createNode(Node node) {
        if (nodes.containsKey(node.identifier())) {
            throw new IllegalArgumentException("Node with id '" + node.identifier() + "' already exists");
        }

        nodes.put(node.identifier(), node);
    }

    @Override
    public void removeNode(Node node) {
        nodes.remove(node.identifier());
    }

    @Override
    public List<Node> allNodes() {
        return nodes.values().stream().toList();
    }

    @Override
    public Optional<Node> findNode(String identifier) {
        return nodes.containsKey(identifier)? Optional.of(nodes.get(identifier)) : Optional.empty();
    }
}
