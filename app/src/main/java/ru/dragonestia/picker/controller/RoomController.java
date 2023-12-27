package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.RoomInfoResponse;
import ru.dragonestia.picker.controller.response.RoomListResponse;
import ru.dragonestia.picker.controller.response.RoomRegisterResponse;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.type.SlotLimit;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("/nodes/{nodeId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final NodeService nodeService;
    private final RoomService roomService;

    @GetMapping
    ResponseEntity<RoomListResponse> all(@PathVariable(name = "nodeId") String nodeId) {
        var nodeOpt = nodeService.find(nodeId);
        return nodeOpt.map(node -> ResponseEntity.ok(new RoomListResponse(nodeId,
                roomService.all(node).stream()
                        .map(room -> new RoomListResponse.RoomDTO(room.getId(), room.getSlots().getSlots(), room.isLocked()))
                        .toList()
        ))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<RoomRegisterResponse> register(@PathVariable(name = "nodeId") String nodeId,
                                                  @RequestParam(name = "roomId") String roomId,
                                                  @RequestParam(name = "slots") int slots,
                                                  @RequestParam(name = "payload") String payload,
                                                  @RequestParam(name = "locked", defaultValue = "false") boolean locked) {

        var nodeOpt = nodeService.find(nodeId);

        if (nodeOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new RoomRegisterResponse(false, "Node does not exist"));
        }

        var room = Room.create(roomId, nodeOpt.get(), SlotLimit.of(slots), payload);
        room.setLocked(locked);
        try {
            roomService.create(room);
            return ResponseEntity.ok(new RoomRegisterResponse(true, ""));
        } catch (Error error) {
            return ResponseEntity.status(400).body(new RoomRegisterResponse(false, error.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new RoomRegisterResponse(false, ex.getMessage()));
        }
    }

    @DeleteMapping("/{roomId}")
    ResponseEntity<?> remove(@PathVariable("nodeId") String nodeId,
                             @PathVariable("roomId") String roomId) {
        if (!NamingValidator.validateNodeId(nodeId) || !NamingValidator.validateRoomId(roomId)) {
            return ResponseEntity.ok().build();
        }

        var nodeOpt = nodeService.find(nodeId);
        nodeOpt.flatMap(node -> roomService.find(node, roomId))
                .ifPresent(roomService::remove);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    ResponseEntity<RoomInfoResponse> info(@PathVariable("nodeId") String nodeId,
                                          @PathVariable("roomId") String roomId) {
        if (!NamingValidator.validateNodeId(nodeId) || !NamingValidator.validateRoomId(roomId)) {
            return ResponseEntity.ok().build();
        }

        var nodeOpt = nodeService.find(nodeId);
        if (nodeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var roomOpt = roomService.find(Objects.requireNonNull(nodeOpt.get()), roomId);
        return roomOpt.map(room -> ResponseEntity.ok(new RoomInfoResponse(room)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{roomId}/lock")
    ResponseEntity<Boolean> lockBucket(@PathVariable("nodeId") String nodeId,
                                 @PathVariable("roomId") String roomId,
                                 @RequestParam(name = "newState") boolean value) {

        if (!NamingValidator.validateNodeId(nodeId) || !NamingValidator.validateRoomId(roomId)) {
            return ResponseEntity.notFound().build();
        }

        var nodeOpt = nodeService.find(nodeId);
        if (nodeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var roomOpt = roomService.find(Objects.requireNonNull(nodeOpt.get()), roomId);
        if (roomOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var room = roomOpt.get();
        room.setLocked(value);
        return ResponseEntity.ok(true);
    }
}
