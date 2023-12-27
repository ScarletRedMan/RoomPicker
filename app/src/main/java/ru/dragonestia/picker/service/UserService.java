package ru.dragonestia.picker.service;

import ru.dragonestia.picker.model.Bucket;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    List<Bucket> getUserBuckets(User user);

    void linkUsersWithBucket(Bucket bucket, Collection<User> users, boolean force);

    void unlinkUsersFromBucket(Bucket bucket, Collection<User> users);

    List<User> getBucketUsers(Bucket bucket);
}
