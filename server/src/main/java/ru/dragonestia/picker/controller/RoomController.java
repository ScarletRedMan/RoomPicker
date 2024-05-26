package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.ResponseObject;
import ru.dragonestia.picker.exception.DoesNotExistsException;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.model.room.factory.RoomFactory;
import ru.dragonestia.picker.service.InstanceService;
import ru.dragonestia.picker.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/instances/target/{instanceId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final InstanceService instanceService;
    private final RoomService roomService;
    private final RoomFactory roomFactory;

    @GetMapping
    List<String> listRooms(@PathVariable String instanceId) {
        return roomService.all(InstanceId.of(instanceId)).stream().map(id -> id.getId().getValue()).toList();
    }

    @GetMapping("/target/{roomId}")
    ResponseObject.Room targetRoomDetails(@PathVariable String instanceId, @PathVariable String roomId) {
        return roomService.find(InstanceId.of(instanceId), RoomId.of(roomId))
                .map(ResponseObject.Room::of)
                .orElseThrow(() -> DoesNotExistsException.forRoom(RoomId.of(roomId)));
    }

    @GetMapping("/list")
    List<ResponseObject.Room> listRoomDetails(@PathVariable String instanceId, @RequestParam List<String> id) {
        return id.stream().map(roomId -> targetRoomDetails(instanceId, roomId)).toList();
    }

    @PostMapping
    ResponseEntity<Void> createRoom(@PathVariable String instanceId,
                                    @RequestParam String id,
                                    @RequestParam int slots,
                                    @RequestParam(defaultValue = "false") boolean locked,
                                    @RequestParam(defaultValue = "false") boolean persist,
                                    @RequestBody(required = false) String payload) {
        var instance = instanceService.find(InstanceId.of(instanceId))
                        .orElseThrow(() -> DoesNotExistsException.forInstance(InstanceId.of(instanceId)));
        roomService.create(roomFactory.create(RoomId.of(id), instance, slots, payload, persist, locked));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/target/{roomId}")
    ResponseEntity<Void> deleteRoom(@PathVariable String instanceId, @PathVariable String roomId) {
        roomService.find(InstanceId.of(instanceId), RoomId.of(roomId)).ifPresent(roomService::remove);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/list")
    ResponseEntity<Void> deleteRooms(@PathVariable String instanceId, @RequestParam List<String> id) {
        for (var roomId: id) {
            deleteRoom(instanceId, roomId);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/target/{roomId}/lock")
    ResponseEntity<Void> lockRoom(@PathVariable String instanceId, @PathVariable String roomId, @RequestParam boolean newState) {
        var room = roomService.find(InstanceId.of(instanceId), RoomId.of(roomId))
                        .orElseThrow(() -> DoesNotExistsException.forRoom(RoomId.of(roomId)));
        room.setLocked(newState);
        roomService.updateState(room);
        return ResponseEntity.ok().build();
    }
}
