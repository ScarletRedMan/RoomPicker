package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Link users with room", hidden = true)
public record LinkUsersWithRoomResponse(
        @Schema(description = "Number of users in room", example = "15") int usedSlots,
        @Schema(description = "Maximum number of users in room", example = "20") int totalSlots
) {}
