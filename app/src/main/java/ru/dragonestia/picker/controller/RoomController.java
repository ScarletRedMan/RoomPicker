package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.RoomInfoResponse;
import ru.dragonestia.picker.controller.response.RoomListResponse;
import ru.dragonestia.picker.exception.NodeNotFoundException;
import ru.dragonestia.picker.exception.RoomNotFoundException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.type.SlotLimit;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.util.NamingValidator;

@Log4j2
@RestController
@RequestMapping("/nodes/{nodeId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final NamingValidator namingValidator;

    @GetMapping
    ResponseEntity<RoomListResponse> all(@PathVariable(name = "nodeId") String nodeId) {

        return nodeService.find(nodeId)
                .map(node -> ResponseEntity.ok(new RoomListResponse(nodeId,
                        roomService.all(node).stream()
                                .map(room -> new RoomListResponse.RoomDTO(room.getId(), room.getSlots().getSlots(), room.isLocked()))
                                .toList()
                ))).orElseThrow(() -> new NodeNotFoundException(nodeId));
    }

    @PostMapping
    ResponseEntity<?> register(@PathVariable(name = "nodeId") String nodeId,
                               @RequestParam(name = "roomId") String roomId,
                               @RequestParam(name = "slots") int slots,
                               @RequestParam(name = "payload") String payload,
                               @RequestParam(name = "locked", defaultValue = "false") boolean locked) {

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        var room = Room.create(roomId, node, SlotLimit.of(slots), payload);
        room.setLocked(locked);
        roomService.create(room);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    ResponseEntity<?> remove(@PathVariable("nodeId") String nodeId,
                             @PathVariable("roomId") String roomId) {

        namingValidator.validateNodeId(nodeId);
        namingValidator.validateRoomId(roomId);

        var nodeOpt = nodeService.find(nodeId);
        nodeOpt.flatMap(node -> roomService.find(node, roomId))
                .ifPresent(roomService::remove);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    ResponseEntity<RoomInfoResponse> info(@PathVariable("nodeId") String nodeId,
                                          @PathVariable("roomId") String roomId) {

        namingValidator.validateNodeId(nodeId);
        namingValidator.validateRoomId(roomId);

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        return roomService.find(node, roomId)
                .map(room -> ResponseEntity.ok(new RoomInfoResponse(room)))
                .orElseThrow(() -> new RoomNotFoundException(nodeId, roomId));
    }

    @PostMapping("/{roomId}/lock")
    ResponseEntity<Boolean> lockBucket(@PathVariable("nodeId") String nodeId,
                                       @PathVariable("roomId") String roomId,
                                       @RequestParam(name = "newState") boolean value) {

        namingValidator.validateNodeId(nodeId);
        namingValidator.validateRoomId(roomId);

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        var room = roomService.find(node, roomId).orElseThrow(() -> new RoomNotFoundException(nodeId, roomId));
        room.setLocked(value);
        return ResponseEntity.ok(true);
    }
}
