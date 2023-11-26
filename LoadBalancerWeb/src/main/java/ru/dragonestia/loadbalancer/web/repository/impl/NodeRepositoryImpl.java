package ru.dragonestia.loadbalancer.web.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.repository.NodeRepository;
import ru.dragonestia.loadbalancer.web.repository.impl.response.NodeDetailsResponse;
import ru.dragonestia.loadbalancer.web.repository.impl.response.NodeListResponse;
import ru.dragonestia.loadbalancer.web.repository.impl.response.NodeRegisterResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

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
