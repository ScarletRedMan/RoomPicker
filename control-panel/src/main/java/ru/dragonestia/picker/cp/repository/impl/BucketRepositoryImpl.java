package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;
import ru.dragonestia.picker.cp.model.Bucket;
import ru.dragonestia.picker.cp.model.Node;
import ru.dragonestia.picker.cp.model.dto.BucketDTO;
import ru.dragonestia.picker.cp.repository.BucketRepository;
import ru.dragonestia.picker.cp.repository.impl.response.BucketInfoResponse;
import ru.dragonestia.picker.cp.repository.impl.response.BucketListResponse;
import ru.dragonestia.picker.cp.repository.impl.response.BucketRegisterResponse;

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
    public List<BucketDTO> all(Node node) {
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
    public void remove(Node node, BucketDTO bucket) {
        rest.delete(URI.create("/nodes/" + node.identifier() + "/buckets/" + bucket.identifier()), params -> {});
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

    @Override
    public void lock(Bucket bucket, boolean value) {
        try {
            rest.post(URI.create(bucket.createApiURI() + "/lock"), Boolean.class, params -> {
                params.put("state", Boolean.toString(value));
            });
        } catch (Exception ex) {
            log.throwing(ex);
            throw new Error("Error when changing bucket locked state");
        }
    }
}
