package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.picker.model.Bucket;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.BucketRepository;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class BucketRepositoryImpl implements BucketRepository {

    private final UserRepository userRepository;
    private final Map<Node, Buckets> node2bucketsMap = new ConcurrentHashMap<>();

    @Override
    public void createBucket(Bucket bucket) {
        var nodeId = bucket.getNodeIdentifier();

        synchronized (node2bucketsMap) {
            var node = node2bucketsMap.keySet().stream()
                    .filter(n -> bucket.getNodeIdentifier().equals(n.identifier()))
                    .findFirst();

            if (node.isEmpty()) {
                throw new IllegalArgumentException("Node '" + nodeId + "' does not exist");
            }

            var buckets = node2bucketsMap.get(node.get());
            if (buckets.containsKey(bucket.getIdentifier())) {
                throw new IllegalArgumentException("Bucket already exists");
            }
            buckets.put(bucket.getIdentifier(), new BucketContainer(bucket, new AtomicInteger(0)));
        }
    }

    @Override
    public void removeBucket(Bucket bucket) {
        var nodeId = bucket.getNodeIdentifier();
        var node = node2bucketsMap.keySet().stream()
                .filter(n -> bucket.getNodeIdentifier().equals(n.identifier()))
                .findFirst();

        synchronized (node2bucketsMap) {
            if (node.isEmpty()) {
                throw new IllegalArgumentException("Node '" + nodeId + "' does not exist");
            }

            node2bucketsMap.get(node.get()).remove(bucket.getIdentifier());
        }

        userRepository.onRemoveBucket(bucket);
    }

    @Override
    public Optional<Bucket> findBucket(Node node, String identifier) {
        synchronized (node2bucketsMap) {
            if (!node2bucketsMap.containsKey(node)) {
                throw new IllegalArgumentException("Node '" + node.identifier() + "' does not exist");
            }

            var result = node2bucketsMap.get(node).getOrDefault(identifier, null);
            return result == null? Optional.empty() : Optional.of(result.bucket());
        }
    }

    @Override
    public List<Bucket> all(Node node) {
        synchronized (node2bucketsMap) {
            return node2bucketsMap.get(node).values().stream().map(BucketContainer::bucket).toList();
        }
    }

    @Override
    public int countAvailableBuckets(Node node, int requiredSlots) {
        return (int) node2bucketsMap.get(node).values().stream()
                .filter(bucket -> bucket.isAvailable(requiredSlots))
                .count();
    }

    @Override
    public Optional<Bucket> pickFreeBucket(Node node, Collection<User> users) {
        synchronized (node2bucketsMap) {
            if (!node2bucketsMap.containsKey(node)) {
                throw new IllegalArgumentException("Node '" + node.identifier() + "' does not exist");
            }

            var requiredSlots = users.size();
            var container = node2bucketsMap.get(node).values().stream() // TODO: pick bucket with used node balancing method
                    .filter(b -> b.isAvailable(requiredSlots))
                    .findFirst();

            if (container.isPresent()) {
                var cont = container.get();
                var addedUsers = userRepository.linkWithBucket(cont.bucket(), users, false);
                cont.used().getAndAdd((int) addedUsers.values().stream().filter(Boolean.TRUE::equals).count());
            }

            return container.map(BucketContainer::bucket);
        }
    }

    @Override
    public void onCreateNode(Node node) {
        synchronized (node2bucketsMap) {
            node2bucketsMap.put(node, new Buckets());
        }
    }

    @Override
    public void onRemoveNode(Node node) {
        synchronized (node2bucketsMap) {
            node2bucketsMap.remove(node);
        }
    }

    private record BucketContainer(Bucket bucket, AtomicInteger used) {

        public boolean isAvailable(int requiredSlots) {
            return bucket.isAvailable(used.get(), requiredSlots);
        }
    }

    private static class Buckets extends LinkedHashMap<String, BucketContainer> {}
}
