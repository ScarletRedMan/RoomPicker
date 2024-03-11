package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;

import java.util.List;

@Schema(title = "Room list", hidden = true)
public record RoomListResponse(
        @Schema(description = "Node identifier", example = "test-node") String node,
        List<ShortResponseRoom> rooms
) {}
