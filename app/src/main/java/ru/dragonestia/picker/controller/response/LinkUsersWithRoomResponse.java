package ru.dragonestia.picker.controller.response;

public record LinkUsersWithRoomResponse(boolean success, String message, int usedSlots, int totalSlots) {}
