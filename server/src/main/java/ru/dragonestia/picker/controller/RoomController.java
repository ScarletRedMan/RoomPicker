package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.ResponseObject;

import java.util.List;

@RestController
@RequestMapping("/instances/target/{instanceId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    @GetMapping
    List<String> listRooms(@PathVariable String instanceId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/target/{roomId}")
    ResponseObject.Room targetRoomDetails(@PathVariable String instanceId, @PathVariable String roomId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/list")
    List<ResponseObject.Room> listRoomDetails(@PathVariable String instanceId, @RequestParam List<String> id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping
    ResponseEntity<Void> createRoom(@PathVariable String instanceId,
                                    @RequestParam String id,
                                    @RequestParam int slots,
                                    @RequestParam String payload,
                                    @RequestParam(defaultValue = "false") boolean locked,
                                    @RequestParam(defaultValue = "false") boolean persist) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/target/{roomId}")
    ResponseEntity<Void> deleteRoom(@PathVariable String instanceId, @PathVariable String roomId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/list")
    ResponseEntity<Void> deleteRooms(@PathVariable String instanceId, @RequestParam List<String> id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PutMapping("/target/{roomId}/lock")
    ResponseEntity<Void> lockRoom(@PathVariable String instanceId, @PathVariable String roomId, @RequestParam boolean newState) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
