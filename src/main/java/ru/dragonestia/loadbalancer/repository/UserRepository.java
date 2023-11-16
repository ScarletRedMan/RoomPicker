package ru.dragonestia.loadbalancer.repository;

import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    void linkWithBucket(Bucket bucket, Collection<User> users);

    void unlinkWithBucket(Bucket bucket, Collection<User> users);

    List<Bucket> findAllLinkedUserBuckets(User user);
}
