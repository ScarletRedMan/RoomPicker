package ru.dragonestia.picker.api.repository.response;

import ru.dragonestia.picker.api.repository.response.type.RRoom;

import java.util.List;

public record RoomListResponse(String node, List<RRoom.Short> rooms) {}
