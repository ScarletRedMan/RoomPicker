package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instances/{instanceId}/rooms/{roomId}/users")
public class EntityRoomController {

    @GetMapping
    List<String> entitiesInsideRoom(@PathVariable String instanceId, @PathVariable String roomId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping
    ResponseEntity<Void> linkEntitiesWithRoom(@PathVariable String instanceId,
                                              @PathVariable String roomId,
                                              @RequestParam List<String> entities,
                                              @RequestParam(defaultValue = "false") boolean force) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping
    ResponseEntity<Void> unlinkEntitiesForBucket(@PathVariable String instanceId,
                                                 @PathVariable String roomId,
                                                 @RequestParam List<String> entities) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
