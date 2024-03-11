package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;

import java.util.List;

@Schema(title = "Linked rooms with user", hidden = true)
public record LinkedRoomsWithUserResponse(List<ShortResponseRoom> rooms) {}
