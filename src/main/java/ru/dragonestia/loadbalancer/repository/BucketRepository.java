package ru.dragonestia.loadbalancer.repository;

import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BucketRepository {

    void createBucket(Bucket bucket);

    void removeBucket(Bucket bucket);

    Optional<Bucket> findBucket(Node node, String identifier);

    List<Bucket> all(Node node);

    default int countAvailableBuckets(Node node) {
        return countAvailableBuckets(node, 0);
    }

    int countAvailableBuckets(Node node, int requiredSlots);

    Optional<Bucket> pickFreeBucket(Node node, Collection<User> users);

    void freeBucket(Bucket bucket, Collection<User> users);

    void onCreateNode(Node node);

    void onRemoveNode(Node node);
}
