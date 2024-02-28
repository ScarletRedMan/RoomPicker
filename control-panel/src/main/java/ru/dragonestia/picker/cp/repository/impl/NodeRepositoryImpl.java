package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.repository.details.NodeDetails;
import ru.dragonestia.picker.api.repository.response.NodeDetailsResponse;
import ru.dragonestia.picker.api.repository.response.NodeListResponse;
import ru.dragonestia.picker.api.repository.response.type.RNode;
import ru.dragonestia.picker.api.repository.NodeRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class NodeRepositoryImpl implements NodeRepository {

    private final RestUtil rest;

    @Override
    public void register(RNode node, boolean persist) throws InvalidNodeIdentifierException, NodeAlreadyExistException {
        rest.query("nodes", HttpMethod.POST, params -> {
            params.put("nodeId", node.getId());
            params.put("method", node.getMode().name());
            params.put("persist", Boolean.toString(persist));
        });
    }

    @Override
    public List<RNode> all(Set<NodeDetails> details) {
        return rest.query("nodes", HttpMethod.GET, NodeListResponse.class, params -> {
            params.put("requiredDetails", String.join(",", details.stream().map(Enum::toString).toList()));
        }).nodes();
    }

    @Override
    public Optional<RNode> find(String nodeId) {
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
