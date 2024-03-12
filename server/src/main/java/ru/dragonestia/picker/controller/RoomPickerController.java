package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;

@Tag(name = "RoomPicker")
@RestController
public class RoomPickerController {

    @Operation(summary = "Server info")
    @GetMapping("/info")
    RoomPickerInfoResponse info() {
        return new RoomPickerInfoResponse("v0.0.1");
    }
}
