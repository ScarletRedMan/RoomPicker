package ru.dragonestia.picker.service;

import ru.dragonestia.picker.model.Bucket;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;

import java.util.List;
import java.util.Optional;

public interface BucketService {

    void createBucket(Bucket bucket);

    void removeBucket(Bucket bucket);

    Optional<Bucket> findBucket(Node node, String identifier);

    List<Bucket> allBuckets(Node node);

    default int countAvailableBuckets(Node node) {
        return countAvailableBuckets(node, 1);
    }

    int countAvailableBuckets(Node node, int requiredSlots);

    Bucket pickAvailableBucket(Node node, List<User> users);
}
