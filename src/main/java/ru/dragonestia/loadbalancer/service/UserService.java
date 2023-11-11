package ru.dragonestia.loadbalancer.service;

import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.User;

import java.util.List;

public interface UserService {

    List<Bucket> getUserBuckets(User user);
}
