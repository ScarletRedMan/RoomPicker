package ru.dragonestia.loadbalancer.web.repository;

import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;

import java.util.List;
import java.util.Optional;

public interface BucketRepository {

    List<Bucket> all(Node node);

    void register(Bucket bucket);

    void remove(Bucket bucket);

    Optional<Bucket> find(Node node, String identifier);

    void lock(Bucket bucket, boolean value);
}
