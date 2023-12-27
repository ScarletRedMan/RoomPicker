package ru.dragonestia.picker.controller.response;

import ru.dragonestia.picker.model.User;

import java.util.List;

public record RoomUserListResponse(int slots, int usedSlots, List<User> users) {}
