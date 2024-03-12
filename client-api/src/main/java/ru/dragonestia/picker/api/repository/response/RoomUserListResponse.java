package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.model.user.ResponseUser;

import java.util.List;

@Schema(title = "Users inside room", hidden = true)
public record RoomUserListResponse(
        @Schema(description = "Number of users in room", example = "15") int slots,
        @Schema(description = "Maximum number of users in room", example = "20") int usedSlots,
        @Schema(description = "Users") List<ResponseUser> users
) {}
