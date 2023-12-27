package ru.dragonestia.picker.controller.response;

import java.util.List;

public record RoomListResponse(String node, List<RoomDTO> rooms) {

    public record RoomDTO(String id, int slots, boolean locked) {}
}
