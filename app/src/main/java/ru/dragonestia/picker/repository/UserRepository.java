package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.model.Bucket;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserRepository {

    Map<User, Boolean> linkWithBucket(Bucket bucket, Collection<User> users, boolean force);

    int unlinkWithBucket(Bucket bucket, Collection<User> users);

    List<Bucket> findAllLinkedUserBuckets(User user);

    void onRemoveBucket(Bucket bucket);

    List<User> usersOf(Bucket bucket);
}
