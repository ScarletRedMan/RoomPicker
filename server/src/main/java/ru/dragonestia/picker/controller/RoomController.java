package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.repository.response.RoomInfoResponse;
import ru.dragonestia.picker.api.repository.response.RoomListResponse;

@Tag(name = "Rooms", description = "Room management")
@RestController
@RequestMapping("/instances/{instanceId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    @Operation(summary = "Get all rooms from node")
    @GetMapping
    ResponseEntity<RoomListResponse> all(
            @Parameter(description = "Instance identifier") @PathVariable(name = "instanceId") String instanceId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Register new room")
    @PostMapping
    ResponseEntity<?> register(
            @Parameter(description = "Instance identifier") @PathVariable(name = "instanceId") String instanceId,
            @Parameter(description = "Room identifier") @RequestParam(name = "roomId") String roomId,
            @Parameter(description = "Maximum users count in room") @RequestParam(name = "slots") int slots,
            @Parameter(description = "Payload. Some data") @RequestParam(name = "payload") String payload,
            @Parameter(description = "Lock for picking") @RequestParam(name = "locked", required = false, defaultValue = "false") boolean locked,
            @Parameter(description = "Save room") @RequestParam(name = "persist", required = false, defaultValue = "false") boolean persist
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Unregister room")
    @DeleteMapping("/{roomId}")
    ResponseEntity<?> remove(
            @Parameter(description = "Instance identifier") @PathVariable("instanceId") String instanceId,
            @Parameter(description = "Room identifier") @PathVariable("roomId") String roomId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Get room details")
    @GetMapping("/{roomId}")
    ResponseEntity<RoomInfoResponse> info(
            @Parameter(description = "Instance identifier") @PathVariable("instanceId") String instanceId,
            @Parameter(description = "Room identifier") @PathVariable("roomId") String roomId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Lock/unlock room")
    @ApiResponse(description = "New lock state")
    @PutMapping("/{roomId}/lock")
    ResponseEntity<Boolean> lockRoom(
            @Parameter(description = "Instance identifier") @PathVariable("instanceId") String instanceId,
            @Parameter(description = "Room identifier") @PathVariable("roomId") String roomId,
            @Parameter(description = "New state for Lock property") @RequestParam(name = "newState") boolean value
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
