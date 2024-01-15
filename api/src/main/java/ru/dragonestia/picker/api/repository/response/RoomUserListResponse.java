package ru.dragonestia.picker.api.repository.response;

import ru.dragonestia.picker.api.model.User;

import java.util.List;

public record RoomUserListResponse(int slots, int usedSlots, List<User> users) {}
