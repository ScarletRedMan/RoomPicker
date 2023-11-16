package ru.dragonestia.loadbalancer.repository.impl;

import org.springframework.stereotype.Repository;
import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.User;
import ru.dragonestia.loadbalancer.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<User, Set<Bucket>> usersMap = new ConcurrentHashMap<>();

    @Override
    public void linkWithBucket(Bucket bucket, Collection<User> users) {
        synchronized (usersMap) {
            for (var user: users) {
                var set = usersMap.getOrDefault(user, new HashSet<>());
                set.add(bucket);
                usersMap.put(user, set);
            }
        }
    }

    @Override
    public void unlinkWithBucket(Bucket bucket, Collection<User> users) {
        synchronized (usersMap) {
            for (var user: users) {
                var set = usersMap.getOrDefault(user, new HashSet<>());
                set.remove(bucket);
                usersMap.put(user, set);
            }
        }
    }

    @Override
    public List<Bucket> findAllLinkedUserBuckets(User user) {
        synchronized (usersMap) {
            return usersMap.getOrDefault(user, new HashSet<>()).stream().toList();
        }
    }
}
