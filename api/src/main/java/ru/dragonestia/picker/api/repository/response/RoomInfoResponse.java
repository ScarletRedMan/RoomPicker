package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.repository.response.type.RRoom;

@Schema(title = "Room info", hidden = true)
public record RoomInfoResponse(RRoom room) {}
