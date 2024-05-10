package ru.dragonestia.picker.controller.graphql.entity;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.controller.graphql.entity.type.DataProvider;
import ru.dragonestia.picker.model.user.User;

import java.util.List;

@RequiredArgsConstructor
public class EntityUser {

    private final User user;
    private final DataProvider dataProvider;
    private List<EntityRoom> cachedRooms = null;

    public @NotNull String getId() {
        return user.getIdentifier();
    }

    public List<EntityRoom> getRooms() {
        if (cachedRooms != null) {
            return cachedRooms;
        }

        cachedRooms = dataProvider.userService().getUserRooms(user).stream()
                .map(room -> new EntityRoom(room, dataProvider))
                .toList();

        return cachedRooms;
    }

    public int getCountRooms() {
        return getRooms().size();
    }
}
