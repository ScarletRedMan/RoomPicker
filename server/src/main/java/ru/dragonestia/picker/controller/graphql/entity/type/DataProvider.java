package ru.dragonestia.picker.controller.graphql.entity.type;

import jakarta.validation.constraints.NotNull;
import ru.dragonestia.picker.service.InstanceService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.UserService;

public record DataProvider(@NotNull InstanceService instanceService,
                           @NotNull RoomService roomService,
                           @NotNull UserService userService) {}
