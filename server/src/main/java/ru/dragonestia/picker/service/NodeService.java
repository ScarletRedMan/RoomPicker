package ru.dragonestia.picker.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.model.node.NodeDetails;
import ru.dragonestia.picker.api.model.node.ResponseNode;
import ru.dragonestia.picker.model.Node;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NodeService {

    @PreAuthorize("hasRole('NODE_MANAGEMENT')")
    void create(Node node) throws InvalidNodeIdentifierException, NodeAlreadyExistException;

    @PreAuthorize("hasRole('NODE_MANAGEMENT')")
    void remove(Node node);

    List<Node> all();

    Optional<Node> find(String nodeId);
}
