package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.repository.response.NodeDetailsResponse;
import ru.dragonestia.picker.api.repository.response.NodeListResponse;
import ru.dragonestia.picker.api.model.Node;
import ru.dragonestia.picker.api.repository.NodeRepository;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class NodeRepositoryImpl implements NodeRepository {

    private final RestUtil rest;

    @Override
    public void register(Node node) throws InvalidNodeIdentifierException, NodeAlreadyExistException {
        rest.query("nodes", HttpMethod.POST, params -> {
            params.put("nodeId", node.getId());
            params.put("method", node.getMode().name());
        });
    }

    @Override
    public List<Node> all() {
        return rest.query("nodes", HttpMethod.GET, NodeListResponse.class, params -> {}).nodes();
    }

    @Override
    public Optional<Node> find(String nodeId) {
        try {
            var response = rest.query("nodes/" + nodeId, HttpMethod.GET, NodeDetailsResponse.class, params -> {});
            return Optional.of(response.node());
        } catch (NodeNotFoundException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(String nodeId) {
        rest.query("nodes/" + nodeId, HttpMethod.DELETE, params -> {});
    }
}
