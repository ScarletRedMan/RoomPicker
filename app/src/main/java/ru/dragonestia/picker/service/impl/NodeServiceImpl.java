package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.repository.details.NodeDetails;
import ru.dragonestia.picker.api.repository.response.type.RNode;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.storage.NodeAndRoomStorage;
import ru.dragonestia.picker.util.DetailsExtractor;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;
    private final DetailsExtractor detailsExtractor;
    private final NamingValidator namingValidator;
    private final NodeAndRoomStorage storage;

    @Override
    public void create(Node node) throws InvalidNodeIdentifierException, NodeAlreadyExistException {
        namingValidator.validateNodeId(node.id());
        nodeRepository.create(node);
        storage.saveNode(node);
    }

    @Override
    public void remove(Node node) {
        for (var room: nodeRepository.delete(node)) {
            storage.removeRoom(room);
        }
        storage.removeNode(node);
    }

    @Override
    public List<Node> all() {
        return nodeRepository.all();
    }

    @Override
    public List<RNode> getAllNodesWithDetailsResponse(Set<NodeDetails> details) {
        var response = new LinkedList<RNode>();
        for (var node: all()) {
            response.add(detailsExtractor.extract(node, details));
        }
        return response;
    }

    @Override
    public Optional<Node> find(String nodeId) {
        return nodeRepository.find(nodeId);
    }
}
