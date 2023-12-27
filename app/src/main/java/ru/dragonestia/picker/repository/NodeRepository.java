package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void createNode(Node node);

    void deleteNode(Node node);

    Optional<Node> findNode(String nodeId);

    List<Node> all();
}
