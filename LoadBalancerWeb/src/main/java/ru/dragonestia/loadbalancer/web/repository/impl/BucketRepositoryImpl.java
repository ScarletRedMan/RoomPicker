package ru.dragonestia.loadbalancer.web.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.repository.BucketRepository;
import ru.dragonestia.loadbalancer.web.repository.impl.response.BucketInfoResponse;
import ru.dragonestia.loadbalancer.web.repository.impl.response.BucketListResponse;
import ru.dragonestia.loadbalancer.web.repository.impl.response.BucketRegisterResponse;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class BucketRepositoryImpl implements BucketRepository {

    private final RestUtil rest;

    @Override
    public List<Bucket> all(Node node) {
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

    @Override
    public void register(Bucket bucket) {
        try {
            var response = rest.post(URI.create("/nodes/" + bucket.getNodeIdentifier() + "/buckets"),
                    BucketRegisterResponse.class,
                    params -> {
                        params.put("identifier", bucket.getIdentifier());
                        params.put("slots", Integer.toString(bucket.getSlots().slots()));
                        params.put("payload", bucket.getPayload());
                        params.put("locked", Boolean.toString(bucket.isLocked()));
                    });

            if (response.success()) return;
            throw new Error(response.message());
        } catch (HttpClientErrorException ex) {
            var response = ex.getResponseBodyAs(BucketRegisterResponse.class);

            if (response != null) {
                throw new Error(response.message());
            }

            log.throwing(ex);
            throw new Error("Internal error. Check logs");
        }
    }

    @Override
    public void remove(Bucket bucket) {
        rest.delete(URI.create("/nodes/" + bucket.getNodeIdentifier() + "/buckets/" + bucket.getIdentifier()), params -> {});
    }

    @Override
    public Optional<Bucket> find(Node node, String identifier) {
        try {
            var response = rest.get(URI.create("/nodes/" + node.identifier() + "/buckets/" + identifier), BucketInfoResponse.class, map -> {});
            return Optional.of(response.bucket());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
