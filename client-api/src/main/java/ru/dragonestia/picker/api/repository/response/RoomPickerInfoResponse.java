package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Server info", hidden = true)
public record RoomPickerInfoResponse(
        @Schema(description = "RoomPicker server version", example = "0.0.1") String version
) {}
