package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void create(Node node);

    void delete(Node node);

    Optional<Node> find(String nodeId);

    List<Node> all();
}
