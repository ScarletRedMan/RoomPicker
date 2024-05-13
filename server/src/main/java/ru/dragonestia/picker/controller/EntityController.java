package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.ResponseObject;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.service.EntityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/entities")
public class EntityController {

    private final EntityService entityService;

    @GetMapping("/search")
    List<String> search(@RequestParam String input) {
        return entityService.searchEntities(EntityId.of(input)).stream().map(id -> id.getId().getValue()).toList();
    }

    @GetMapping("/target/rooms")
    List<ResponseObject.Room> find(@RequestParam String id) {
        return entityService.getEntityRooms(EntityId.of(id)).stream()
                .map(ResponseObject.Room::of)
                .toList();
    }

    @GetMapping("/list/rooms")
    Map<String, List<ResponseObject.Room>> roomsOf(@RequestParam List<String> id) {
        var map = new HashMap<String, List<ResponseObject.Room>>();
        for (var userId: id) {
            map.put(userId, find(userId));
        }
        return map;
    }
}
