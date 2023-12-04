package ru.dragonestia.loadbalancer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.User;
import ru.dragonestia.loadbalancer.repository.BucketRepository;
import ru.dragonestia.loadbalancer.service.BucketService;
import ru.dragonestia.loadbalancer.util.NamingValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;

    @Override
    public void createBucket(Bucket bucket) {
        if (!NamingValidator.validateBucketIdentifier(bucket.getIdentifier())) {
            throw new Error("Invalid bucket identifier format");
        }

        bucketRepository.createBucket(bucket);
    }

    @Override
    public void removeBucket(Bucket bucket) {
        bucketRepository.removeBucket(bucket);
    }

    @Override
    public Optional<Bucket> findBucket(Node node, String identifier) {
        return bucketRepository.findBucket(node, identifier);
    }

    @Override
    public List<Bucket> allBuckets(Node node) {
        return bucketRepository.all(node);
    }

    @Override
    public int countAvailableBuckets(Node node, int requiredSlots) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Bucket pickAvailableBucket(Node node, List<User> users) {
        throw new RuntimeException("Not implemented");
    }
}
