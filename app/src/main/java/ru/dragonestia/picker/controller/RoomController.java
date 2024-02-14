package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.repository.response.RoomInfoResponse;
import ru.dragonestia.picker.api.repository.response.RoomListResponse;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.type.SlotLimit;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.util.DetailsParser;
import ru.dragonestia.picker.util.NamingValidator;

@Tag(name = "Rooms", description = "Room management")
@RestController
@RequestMapping("/nodes/{nodeId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final NamingValidator namingValidator;
    private final DetailsParser detailsParser;

    @Operation(summary = "Get all rooms from node")
    @GetMapping
    ResponseEntity<RoomListResponse> all(
            @Parameter(description = "Node identifier") @PathVariable(name = "nodeId") String nodeId,
            @Parameter(description = "Required addition data", example = "COUNT_USERS") @RequestParam(name = "requiredDetails", required = false, defaultValue = "") String detailsSeq
    ) {
        return nodeService.find(nodeId)
                .map(node -> {
                    var details = detailsParser.parseRoomDetails(detailsSeq);
                    var response = new RoomListResponse(nodeId, roomService.getAllRoomsWithDetailsResponse(node, details));
                    return ResponseEntity.ok(response);
                }).orElseThrow(() -> new NodeNotFoundException(nodeId));
    }

    @Operation(summary = "Register new room")
    @PostMapping
    ResponseEntity<?> register(
            @Parameter(description = "Node identifier") @PathVariable(name = "nodeId") String nodeId,
            @Parameter(description = "Room identifier") @RequestParam(name = "roomId") String roomId,
            @Parameter(description = "Maximum users count in room") @RequestParam(name = "slots") int slots,
            @Parameter(description = "Payload. Some data") @RequestParam(name = "payload") String payload,
            @Parameter(description = "Lock for picking") @RequestParam(name = "locked", defaultValue = "false") boolean locked
    ) {
        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        var room = Room.create(roomId, node, SlotLimit.of(slots), payload);
        room.setLocked(locked);
        roomService.create(room);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unregister room")
    @DeleteMapping("/{roomId}")
    ResponseEntity<?> remove(
            @Parameter(description = "Node identifier") @PathVariable("nodeId") String nodeId,
            @Parameter(description = "Room identifier") @PathVariable("roomId") String roomId
    ) {
        namingValidator.validateNodeId(nodeId);
        namingValidator.validateRoomId(nodeId, roomId);

        var nodeOpt = nodeService.find(nodeId);
        nodeOpt.flatMap(node -> roomService.find(node, roomId))
                .ifPresent(roomService::remove);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get room details")
    @GetMapping("/{roomId}")
    ResponseEntity<RoomInfoResponse> info(
            @Parameter(description = "Node identifier") @PathVariable("nodeId") String nodeId,
            @Parameter(description = "Room identifier") @PathVariable("roomId") String roomId
    ) {
        namingValidator.validateNodeId(nodeId);
        namingValidator.validateRoomId(nodeId, roomId);

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        return roomService.find(node, roomId)
                .map(room -> ResponseEntity.ok(new RoomInfoResponse(room.toResponseObject())))
                .orElseThrow(() -> new RoomNotFoundException(nodeId, roomId));
    }

    @Operation(summary = "Lock/unlock room")
    @ApiResponse(description = "New lock state")
    @PutMapping("/{roomId}/lock")
    ResponseEntity<Boolean> lockRoom(
            @Parameter(description = "Node identifier") @PathVariable("nodeId") String nodeId,
            @Parameter(description = "Room identifier") @PathVariable("roomId") String roomId,
            @Parameter(description = "New state for Lock property") @RequestParam(name = "newState") boolean value
    ) {
        namingValidator.validateNodeId(nodeId);
        namingValidator.validateRoomId(nodeId, roomId);

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        var room = roomService.find(node, roomId).orElseThrow(() -> new RoomNotFoundException(nodeId, roomId));
        room.setLocked(value);
        return ResponseEntity.ok(true);
    }
}
