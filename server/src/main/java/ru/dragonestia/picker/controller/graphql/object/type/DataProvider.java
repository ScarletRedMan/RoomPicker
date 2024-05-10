package ru.dragonestia.picker.controller.graphql.object.type;

import jakarta.validation.constraints.NotNull;
import ru.dragonestia.picker.service.InstanceService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.EntityService;

public record DataProvider(@NotNull InstanceService instanceService,
                           @NotNull RoomService roomService,
                           @NotNull EntityService entityService) {}
