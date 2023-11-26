package ru.dragonestia.loadbalancer.web.repository;

import ru.dragonestia.loadbalancer.web.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void registerNode(Node node);

    List<Node> getNodes();

    Optional<Node> findNode(String identifier);

    void removeNode(String identifier);
}
