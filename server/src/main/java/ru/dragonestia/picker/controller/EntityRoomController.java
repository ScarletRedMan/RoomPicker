package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.repository.response.LinkUsersWithRoomResponse;
import ru.dragonestia.picker.api.repository.response.RoomUserListResponse;

@Tag(name = "Users", description = "Entity management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/instances/{instanceId}/rooms/{roomId}/users")
public class EntityRoomController {

    @Operation(summary = "Get users inside room")
    @GetMapping
    ResponseEntity<RoomUserListResponse> usersInsideRoom(
            @Parameter(description = "Instance identifier") @PathVariable(name = "instanceId") String instanceId,
            @Parameter(description = "Room identifier") @PathVariable(name = "roomId") String roomId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Link users with room")
    @PostMapping
    ResponseEntity<LinkUsersWithRoomResponse> linkUserWithRoom(
            @Parameter(description = "Instance identifier") @PathVariable(name = "instanceId") String instanceId,
            @Parameter(description = "Room identifier") @PathVariable(name = "roomId") String roomId,
            @Parameter(description = "Entity identifiers", example = "user1,user2,user3") @RequestParam(name = "userIds") String userIds,
            @Parameter(description = "Ignore slot limitation") @RequestParam(name = "force") boolean force
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Unlink users from room")
    @DeleteMapping
    ResponseEntity<?> unlinkUsersForBucket(
            @Parameter(description = "Instance identifier") @PathVariable(name = "instanceId") String instanceId,
            @Parameter(description = "Room identifier") @PathVariable(name = "roomId") String roomId,
            @Parameter(description = "Entity identifiers", example = "user1,user2,user3") @RequestParam(name = "userIds") String userIds
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
