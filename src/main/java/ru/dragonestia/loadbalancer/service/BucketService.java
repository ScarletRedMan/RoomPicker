package ru.dragonestia.loadbalancer.service;

import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.User;

import java.util.List;
import java.util.Optional;

public interface BucketService {

    void createBucket(Bucket lobby);

    void removeBucket(Bucket lobby);

    Optional<Bucket> findBucket(Node node, String identifier);

    List<Bucket> allBuckets(Node node);

    default int countAvailableBuckets(Node node) {
        return countAvailableBuckets(node, 1);
    }

    int countAvailableBuckets(Node node, int requiredSlots);

    Bucket pickAvailableBucket(Node node, List<User> users);

    void freeBucket(Bucket bucket, List<User> users);
}
