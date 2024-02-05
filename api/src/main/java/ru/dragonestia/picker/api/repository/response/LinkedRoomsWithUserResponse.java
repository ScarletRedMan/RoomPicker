package ru.dragonestia.picker.api.repository.response;

import ru.dragonestia.picker.api.repository.response.type.RRoom;

import java.util.List;

public record LinkedRoomsWithUserResponse(List<RRoom.Short> rooms) {}
