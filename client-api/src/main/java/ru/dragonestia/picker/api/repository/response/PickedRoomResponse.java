package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(title = "Result of picking room", hidden = true)
public record PickedRoomResponse(
        @Schema(description = "Node identifier", example = "test-node") String nodeId,
        @Schema(description = "Room identifier", example = "test-room") String roomId,
        @Schema(description = "Payload", example = "Hello world!") String payload,
        @Schema(description = "Max slots in room", example = "25") int slots,
        @Schema(description = "Used slots in room", example = "5") int usedSlots,
        @Schema(description = "Locked for picking?", example = "false") boolean locked,
        @Schema(description = "User identifiers") Set<String> users
) {}
