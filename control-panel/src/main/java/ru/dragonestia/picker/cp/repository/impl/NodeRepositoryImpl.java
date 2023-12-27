package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.dragonestia.picker.cp.model.Node;
import ru.dragonestia.picker.cp.repository.NodeRepository;
import ru.dragonestia.picker.cp.repository.impl.response.NodeDetailsResponse;
import ru.dragonestia.picker.cp.repository.impl.response.NodeListResponse;
import ru.dragonestia.picker.cp.repository.impl.response.NodeRegisterResponse;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class NodeRepositoryImpl implements NodeRepository {

    private final RestUtil rest;

    @Override
    public void registerNode(Node node) {
        NodeRegisterResponse response;
        try {
            response = rest.post(URI.create("nodes"),
                    NodeRegisterResponse.class,
                    params -> {
                        params.put("identifier", node.identifier());
                        params.put("method", node.method().name());
                    });
        } catch (Exception ex) {
            throw new RuntimeException("Internal error", ex);
        }

        if (!response.success()) {
            throw new Error(response.message());
        }
    }

    @Override
    public List<Node> getNodes() {
        return rest.get(URI.create("nodes"), NodeListResponse.class).nodes();
    }

    @Override
    public Optional<Node> findNode(String identifier) {
        try {
            var response = rest.get(URI.create("nodes/" + identifier), NodeDetailsResponse.class);
            return Optional.of(response.node());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public void removeNode(String identifier) {
        rest.delete(URI.create("nodes/" + identifier), params -> {});
    }
}
