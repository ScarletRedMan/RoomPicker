package ru.dragonestia.loadbalancer.web.repository;

import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.model.dto.BucketDTO;

import java.util.List;
import java.util.Optional;

public interface BucketRepository {

    List<BucketDTO> all(Node node);

    void register(Bucket bucket);

    void remove(Bucket bucket);

    void remove(Node node, BucketDTO bucket);

    Optional<Bucket> find(Node node, String identifier);

    void lock(Bucket bucket, boolean value);
}
