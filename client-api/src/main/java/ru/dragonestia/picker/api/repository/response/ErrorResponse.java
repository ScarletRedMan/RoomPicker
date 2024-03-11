package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(
        name = "ErrorResponse",
        description = "Response with error info"
)
public record ErrorResponse(String errorId,
                            String message,
                            Map<String, String> details) {}
