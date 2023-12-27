package ru.dragonestia.picker.cp.repository.impl.response;

import ru.dragonestia.picker.cp.model.User;

import java.util.List;

public record BucketUserListResponse(int slots, int usedSlots, List<User> users) {}
