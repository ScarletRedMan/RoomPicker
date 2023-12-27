package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.RoomUserListResponse;
import ru.dragonestia.picker.controller.response.LinkUsersWithRoomResponse;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.LinkedList;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/nodes/{nodeId}/rooms/{roomId}/users")
public class UserRoomController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final UserService userService;

    @GetMapping
    ResponseEntity<RoomUserListResponse> usersInsideRoom(@PathVariable(name = "nodeId") String nodeId,
                                                         @PathVariable(name = "roomId") String bucketId) {

        Room room;
        try {
            var temp = getNodeAndRoom(nodeId, bucketId);
            room = temp.room();
        } catch (Error error) {
            return ResponseEntity.notFound().build();
        }

        var users = userService.getRoomUsers(room);
        return ResponseEntity.ok(new RoomUserListResponse(room.getSlots().getSlots(), users.size(), users));
    }

    @PostMapping
    ResponseEntity<LinkUsersWithRoomResponse> linkUserWithRoom(@PathVariable(name = "nodeId") String nodeId,
                                                               @PathVariable(name = "roomId") String roomId,
                                                               @RequestParam(name = "userIds") String userIds,
                                                               @RequestParam(name = "force") boolean force) {

        Room room;
        try {
            var temp = getNodeAndRoom(nodeId, roomId);
            room = temp.room();
        } catch (Error error) {
            return ResponseEntity.status(404).body(new LinkUsersWithRoomResponse(false, error.getMessage()));
        }

        var list = new LinkedList<User>();
        for (var username: userIds.split(",")) {
            if (!NamingValidator.validateUserId(username)) continue;

            list.add(new User(username));
        }

        try {
            userService.linkUsersWithRoom(room, list, force);
        } catch (Error error) {
            return ResponseEntity.status(400).body(new LinkUsersWithRoomResponse(false, error.getMessage()));
        }

        return ResponseEntity.ok(new LinkUsersWithRoomResponse(true, "Success"));
    }

    @DeleteMapping
    ResponseEntity<?> unlinkUsersForBucket(@PathVariable(name = "nodeId") String nodeId,
                                @PathVariable(name = "roomId") String roomId,
                                @RequestParam(name = "userIds") String userIds) {

        Node node;
        Room room;
        try {
            var temp = getNodeAndRoom(nodeId, roomId);
            node = temp.node();
            room = temp.room();
        } catch (Error error) {
            return ResponseEntity.notFound().build();
        }

        return null;
    }

    private record NodeAndRoom(Node node, Room room) {}

    private NodeAndRoom getNodeAndRoom(String nodeId, String roomId) {
        if (!NamingValidator.validateNodeId(nodeId) || !NamingValidator.validateRoomId(roomId)) {
            throw new Error();
        }

        var nodeOpt = nodeService.find(nodeId);
        if (nodeOpt.isEmpty()) {
            throw new Error();
        }

        var roomOpt = roomService.find(Objects.requireNonNull(nodeOpt.get()), roomId);
        if (roomOpt.isEmpty()) {
            throw new Error();
        }

        return new NodeAndRoom(nodeOpt.get(), roomOpt.get());
    }
}
