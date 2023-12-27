package ru.dragonestia.picker.cp.repository;

import ru.dragonestia.picker.cp.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void registerNode(Node node);

    List<Node> getNodes();

    Optional<Node> findNode(String identifier);

    void removeNode(String identifier);
}
