package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.repository.response.LinkUsersWithRoomResponse;
import ru.dragonestia.picker.api.repository.response.RoomUserListResponse;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.Arrays;

@RequiredArgsConstructor
@RestController
@RequestMapping("/nodes/{nodeId}/rooms/{roomId}/users")
public class UserRoomController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final UserService userService;
    private final NamingValidator namingValidator;

    @GetMapping
    ResponseEntity<RoomUserListResponse> usersInsideRoom(@PathVariable(name = "nodeId") String nodeId,
                                                         @PathVariable(name = "roomId") String roomId) {

        var room = getNodeAndRoom(nodeId, roomId).room();
        var users = userService.getRoomUsers(room);
        return ResponseEntity.ok(new RoomUserListResponse(room.getSlots().getSlots(), users.size(), users.stream().map(User::toResponseObject).toList()));
    }

    @PostMapping
    ResponseEntity<LinkUsersWithRoomResponse> linkUserWithRoom(@PathVariable(name = "nodeId") String nodeId,
                                                               @PathVariable(name = "roomId") String roomId,
                                                               @RequestParam(name = "userIds") String userIds,
                                                               @RequestParam(name = "force") boolean force) {

        var room = getNodeAndRoom(nodeId, roomId).room();
        var users = namingValidator.validateUserIds(Arrays.stream(userIds.split(",")).toList());
        var usedSlots = userService.linkUsersWithRoom(room, users, force);
        return ResponseEntity.ok(new LinkUsersWithRoomResponse(usedSlots, room.getSlots().getSlots()));
    }

    @DeleteMapping
    ResponseEntity<?> unlinkUsersForBucket(@PathVariable(name = "nodeId") String nodeId,
                                @PathVariable(name = "roomId") String roomId,
                                @RequestParam(name = "userIds") String userIds) {

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
