package ru.dragonestia.picker.controller.graphql.entity.type;

import jakarta.validation.constraints.NotNull;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.UserService;

public record DataProvider(@NotNull NodeService nodeService,
                           @NotNull RoomService roomService,
                           @NotNull UserService userService) {}
