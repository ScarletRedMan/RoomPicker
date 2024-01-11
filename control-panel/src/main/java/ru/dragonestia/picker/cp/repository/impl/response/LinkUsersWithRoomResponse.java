package ru.dragonestia.picker.cp.repository.impl.response;

public record LinkUsersWithRoomResponse(boolean success, String message, int usedSlots, int totalSlots) {}
