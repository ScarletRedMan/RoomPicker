package ru.dragonestia.picker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;

@RestController
public class RoomPickerController {

    @GetMapping("/info")
    RoomPickerInfoResponse info() {
        return new RoomPickerInfoResponse("v0.0.1");
    }
}
