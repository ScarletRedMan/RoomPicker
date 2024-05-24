package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.exception.DoesNotExistsException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.service.EntityService;
import ru.dragonestia.picker.service.RoomService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instances/{instanceId}/rooms/target/{roomId}/users")
public class EntityRoomController {

    private final RoomService roomService;
    private final EntityService entityService;

    @GetMapping
    List<String> entitiesInsideRoom(@PathVariable String instanceId, @PathVariable String roomId) {
        var room = roomService.find(InstanceId.of(instanceId), RoomId.of(roomId))
                .orElseThrow(() -> DoesNotExistsException.forRoom(RoomId.of(roomId)));
        return entityService.getRoomEntities(room).stream().map(id -> id.getId().getValue()).toList();
    }

    @PostMapping
    ResponseEntity<Void> linkEntitiesWithRoom(@PathVariable String instanceId,
                                              @PathVariable String roomId,
                                              @RequestParam List<String> entities,
                                              @RequestParam(defaultValue = "false") boolean force) {
        var room = roomService.find(InstanceId.of(instanceId), RoomId.of(roomId))
                .orElseThrow(() -> DoesNotExistsException.forRoom(RoomId.of(roomId)));
        entityService.linkEntitiesWithRoom(room, entities.stream().map(EntityId::of).toList(), force);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    ResponseEntity<Void> unlinkEntitiesForBucket(@PathVariable String instanceId,
                                                 @PathVariable String roomId,
                                                 @RequestParam List<String> entities) {
        var room = roomService.find(InstanceId.of(instanceId), RoomId.of(roomId))
                .orElseThrow(() -> DoesNotExistsException.forRoom(RoomId.of(roomId)));
        entityService.unlinkEntitiesFromRoom(room, entities.stream().map(EntityId::of).toList());
        return ResponseEntity.ok().build();
    }
}
