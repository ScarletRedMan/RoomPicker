package ru.dragonestia.picker.cp.repository.impl.response;

import ru.dragonestia.picker.cp.model.dto.RoomDTO;

import java.util.List;

public record RoomListResponse(String node, List<RoomDTO> rooms) {}
