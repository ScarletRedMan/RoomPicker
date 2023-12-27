package ru.dragonestia.loadbalancer.repository.impl;

import org.springframework.stereotype.Repository;
import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.User;
import ru.dragonestia.loadbalancer.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<User, Set<Bucket>> usersMap = new ConcurrentHashMap<>();
    private final Map<NodeBucketPath, Set<User>> bucketUsers = new ConcurrentHashMap<>();

    @Override
    public Map<User, Boolean> linkWithBucket(Bucket bucket, Collection<User> users, boolean force) {
        var result = new HashMap<User, Boolean>();

        synchronized (usersMap) {
            var path = new NodeBucketPath(bucket.getNodeIdentifier(), bucket.getIdentifier());
            var usersSet = bucketUsers.getOrDefault(path, new HashSet<>());

            if (force || bucket.getSlots().isUnlimited()) {
                users.forEach(user -> result.put(user, true));
            } else {
                for (var user : users) {
                    var set = usersMap.getOrDefault(user, new HashSet<>());
                    result.put(user, !set.contains(bucket));
                }

                if (bucket.getSlots().getSlots() < usersSet.size() + users.size()) {
                    throw new Error("Bucket are full");
                }
            }

            for (var user: users) {
                var set = usersMap.getOrDefault(user, new HashSet<>());
                set.add(bucket);
                usersMap.put(user, set);
            }

            usersSet.addAll(users);
            bucketUsers.put(path, usersSet);
        }

        return result;
    }

    @Override
    public int unlinkWithBucket(Bucket bucket, Collection<User> users) {
        var counter = new AtomicInteger();
        synchronized (usersMap) {
            usersMap.forEach((user, set) -> {
                if (!set.contains(bucket)) return;

                set.remove(bucket);
                counter.incrementAndGet();

                if (set.isEmpty()) {
                    usersMap.remove(user);
                }
            });

            var path = new NodeBucketPath(bucket.getNodeIdentifier(), bucket.getIdentifier());
            var set = bucketUsers.getOrDefault(path, new HashSet<>());
            set.removeAll(users);
            if (set.isEmpty()) {
                bucketUsers.remove(path);
            } else {
                bucketUsers.put(path, set);
            }
        }
        return counter.get();
    }

    @Override
    public List<Bucket> findAllLinkedUserBuckets(User user) {
        synchronized (usersMap) {
            return usersMap.getOrDefault(user, new HashSet<>()).stream().toList();
        }
    }

    @Override
    public void onRemoveBucket(Bucket bucket) {
        synchronized (usersMap) {
            usersMap.forEach((user, set) -> {
                set.remove(bucket);
                if (set.isEmpty()) {
                    usersMap.remove(user);
                }
            });
        }
    }

    @Override
    public List<User> usersOf(Bucket bucket) {
        synchronized (usersMap) {
            return bucketUsers.getOrDefault(new NodeBucketPath(bucket.getNodeIdentifier(), bucket.getIdentifier()), new HashSet<>())
                    .stream()
                    .toList();
        }
    }

    private record NodeBucketPath(String node, String bucket) {

        @Override
        public int hashCode() {
            return Objects.hash(node, bucket);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (o instanceof NodeBucketPath other) {
                return other.node().equals(node()) && other.bucket().equals(bucket());
            }
            return false;
        }
    }
}
