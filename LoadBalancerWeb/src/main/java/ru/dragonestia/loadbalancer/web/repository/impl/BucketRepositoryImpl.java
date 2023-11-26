package ru.dragonestia.loadbalancer.web.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.repository.BucketRepository;
import ru.dragonestia.loadbalancer.web.repository.impl.response.BucketListResponse;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class BucketRepositoryImpl implements BucketRepository {

    private final RestUtil rest;

    @Override
    public List<BucketInfo> all(Node node) {
        var entity = rest.getEntity(URI.create("/nodes/" + node.identifier() + "/buckets"),
                BucketListResponse.class);

        if (entity.getStatusCode().value() == 404) {
            throw new Error("Node with identifier '" + node.identifier() + "' does not exists'");
        }

        if (!entity.hasBody()) {
            throw new Error("Bucket list did not present");
        }

        return Objects.requireNonNull(entity.getBody()).buckets();
    }
}
