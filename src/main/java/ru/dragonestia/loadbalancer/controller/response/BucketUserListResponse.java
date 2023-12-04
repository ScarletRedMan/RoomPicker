package ru.dragonestia.loadbalancer.controller.response;

import ru.dragonestia.loadbalancer.model.User;

import java.util.List;

public record BucketUserListResponse(int slots, int usedSlots, List<User> users) {}
