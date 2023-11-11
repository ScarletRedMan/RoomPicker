package ru.dragonestia.loadbalancer.service;

import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.User;

import java.util.List;

public interface BucketService {

    void createBucket(Node node, Bucket lobby);

    void removeBucket(Node node, Bucket lobby);

    List<Bucket> allBuckets(Node node);

    int countAvailableBuckets(Node node);

    Bucket pickAvailableBucket(Node node, List<User> users);

    void freeBucket(Node node, List<User> users);
}
