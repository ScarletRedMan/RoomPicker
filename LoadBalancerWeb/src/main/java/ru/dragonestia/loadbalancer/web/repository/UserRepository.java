package ru.dragonestia.loadbalancer.web.repository;

import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    void linkWithBucket(Bucket bucket, Collection<User> users);

    void unlinkFromBucket(Bucket bucket, Collection<User> users);

    List<User> all(Bucket bucket);
}
