package ru.dragonestia.loadbalancer.cp.repository;

import ru.dragonestia.loadbalancer.cp.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void registerNode(Node node);

    List<Node> getNodes();

    Optional<Node> findNode(String identifier);

    void removeNode(String identifier);
}
