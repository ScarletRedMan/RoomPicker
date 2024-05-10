package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.node.Node;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.storage.NodeAndRoomStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final NodeAndRoomStorage storage;

    @Override
    public void create(Node node) throws InvalidNodeIdentifierException, NodeAlreadyExistException {
        nodeRepository.create(node);
        storage.saveNode(node);
    }

    @Override
    public void remove(Node node) {
        for (var room: roomRepository.all(node)) {
            storage.removeRoom(room);
        }

        nodeRepository.delete(node);
        storage.removeNode(node);
    }

    @Override
    public List<Node> all() {
        return nodeRepository.all();
    }

    @Override
    public Optional<Node> find(String nodeId) {
        return nodeRepository.findById(nodeId);
    }
}
