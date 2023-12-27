package ru.dragonestia.loadbalancer.repository;

import ru.dragonestia.loadbalancer.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void createNode(Node node);

    void deleteNode(Node node);

    Optional<Node> findNode(String nodeId);

    List<Node> all();
}
