package ru.dragonestia.picker.api.repository.response;

import ru.dragonestia.picker.api.model.Room;

import java.util.List;

public record RoomListResponse(String node, List<Room.Short> rooms) {}
