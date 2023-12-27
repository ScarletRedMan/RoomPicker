package ru.dragonestia.loadbalancer.cp.repository;

import ru.dragonestia.loadbalancer.cp.model.Bucket;
import ru.dragonestia.loadbalancer.cp.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    void linkWithBucket(Bucket bucket, Collection<User> users);

    void unlinkFromBucket(Bucket bucket, Collection<User> users);

    List<User> all(Bucket bucket);
}
