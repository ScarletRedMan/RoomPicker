package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/entities")
public class EntityController {

    @GetMapping("/search")
    List<String> search(@RequestParam String input) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/target/rooms")
    List<String> find(@RequestParam String id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/list/rooms")
    Map<String, List<String>> roomsOf(@RequestParam List<String> id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
