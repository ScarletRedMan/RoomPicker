package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.repository.response.LinkUsersWithRoomResponse;
import ru.dragonestia.picker.api.repository.response.RoomUserListResponse;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.DetailsParser;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.Arrays;

@Tag(name = "Users", description = "User management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/nodes/{nodeId}/rooms/{roomId}/users")
public class UserRoomController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final UserService userService;
    private final NamingValidator namingValidator;
    private final DetailsParser detailsParser;

    @Operation(summary = "Get users inside room")
    @GetMapping
    ResponseEntity<RoomUserListResponse> usersInsideRoom(
            @Parameter(description = "Node identifier") @PathVariable(name = "nodeId") String nodeId,
            @Parameter(description = "Room identifier") @PathVariable(name = "roomId") String roomId,
            @Parameter(description = "Required addition user data", example = "COUNT_ROOMS") @RequestParam(name = "requiredDetails", required = false, defaultValue = "") String detailsSeq
    ) {
        var room = getNodeAndRoom(nodeId, roomId).room();
        var users = userService.getRoomUsersWithDetailsResponse(room, detailsParser.parseUserDetails(detailsSeq));

        return ResponseEntity.ok(new RoomUserListResponse(room.getMaxSlots(), users.size(), users));
    }

    @Operation(summary = "Link users with room")
    @PostMapping
    ResponseEntity<LinkUsersWithRoomResponse> linkUserWithRoom(
            @Parameter(description = "Node identifier") @PathVariable(name = "nodeId") String nodeId,
            @Parameter(description = "Room identifier") @PathVariable(name = "roomId") String roomId,
            @Parameter(description = "User identifiers", example = "user1,user2,user3") @RequestParam(name = "userIds") String userIds,
            @Parameter(description = "Ignore slot limitation") @RequestParam(name = "force") boolean force
    ) {
        var room = getNodeAndRoom(nodeId, roomId).room();
        var users = namingValidator.validateUserIds(Arrays.stream(userIds.split(",")).toList());
        userService.linkUsersWithRoom(room, users, force);
        var usedSlots = userService.getRoomUsers(room).size();
        return ResponseEntity.ok(new LinkUsersWithRoomResponse(usedSlots, room.getMaxSlots()));
    }

    @Operation(summary = "Unlink users from room")
    @DeleteMapping
    ResponseEntity<?> unlinkUsersForBucket(
            @Parameter(description = "Node identifier") @PathVariable(name = "nodeId") String nodeId,
            @Parameter(description = "Room identifier") @PathVariable(name = "roomId") String roomId,
            @Parameter(description = "User identifiers", example = "user1,user2,user3") @RequestParam(name = "userIds") String userIds
    ) {

        var room = getNodeAndRoom(nodeId, roomId).room();
        var users = namingValidator.validateUserIds(Arrays.stream(userIds.split(",")).toList());
        userService.unlinkUsersFromRoom(room, users);
        return ResponseEntity.ok().build();
    }

    private record NodeAndRoom(Node node, Room room) {}

    private NodeAndRoom getNodeAndRoom(String nodeId, String roomId) {
        namingValidator.validateNodeId(nodeId);
        namingValidator.validateRoomId(nodeId, roomId);

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        var room = roomService.find(node, roomId).orElseThrow(() -> new RoomNotFoundException(nodeId, roomId));

        return new NodeAndRoom(node, room);
    }
}
