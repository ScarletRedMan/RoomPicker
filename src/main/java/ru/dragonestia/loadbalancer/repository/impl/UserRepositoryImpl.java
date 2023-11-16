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

    @Override
    public Map<User, Boolean> linkWithBucket(Bucket bucket, Collection<User> users) {
        var result = new HashMap<User, Boolean>();

        synchronized (usersMap) {
            for (var user: users) {
                var set = usersMap.getOrDefault(user, new HashSet<>());
                result.put(user, set.add(bucket));
                usersMap.put(user, set);
            }
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
}
