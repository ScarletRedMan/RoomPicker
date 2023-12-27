package ru.dragonestia.loadbalancer.cp.repository.impl.response;

import ru.dragonestia.loadbalancer.cp.model.User;

import java.util.List;

public record BucketUserListResponse(int slots, int usedSlots, List<User> users) {}
