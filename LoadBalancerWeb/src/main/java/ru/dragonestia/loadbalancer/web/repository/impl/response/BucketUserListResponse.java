package ru.dragonestia.loadbalancer.web.repository.impl.response;

import ru.dragonestia.loadbalancer.web.model.User;

import java.util.List;

public record BucketUserListResponse(int slots, int usedSlots, List<User> users) {}
