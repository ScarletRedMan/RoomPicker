package ru.dragonestia.loadbalancer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.User;
import ru.dragonestia.loadbalancer.repository.UserRepository;
import ru.dragonestia.loadbalancer.service.UserService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<Bucket> getUserBuckets(User user) {
        return userRepository.findAllLinkedUserBuckets(user);
    }

    @Override
    public void linkUsersWithBucket(Bucket bucket, Collection<User> users, boolean force) {
        userRepository.linkWithBucket(bucket, users, force);
    }

    @Override
    public void unlinkUsersFromBucket(Bucket bucket, Collection<User> users) {
        userRepository.unlinkWithBucket(bucket, users);
    }

    @Override
    public List<User> getBucketUsers(Bucket bucket) {
        return userRepository.usersOf(bucket);
    }
}
