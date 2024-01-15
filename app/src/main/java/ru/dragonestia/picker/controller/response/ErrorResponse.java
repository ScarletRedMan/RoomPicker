package ru.dragonestia.picker.controller.response;

import java.util.Map;

public record ErrorResponse(String errorId, String message, Map<String, String> details) {}
