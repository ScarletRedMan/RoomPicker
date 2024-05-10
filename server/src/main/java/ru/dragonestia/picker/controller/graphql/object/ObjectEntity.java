package ru.dragonestia.picker.controller.graphql.object;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.controller.graphql.object.type.DataProvider;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.List;

@RequiredArgsConstructor
public class ObjectEntity {

    private final Entity entity;
    private final DataProvider dataProvider;
    private List<ObjectRoom> cachedRooms = null;

    public @NotNull String getId() {
        return entity.getIdentifier();
    }

    public List<ObjectRoom> getRooms() {
        if (cachedRooms != null) {
            return cachedRooms;
        }

        cachedRooms = dataProvider.entityService().getEntityRooms(entity).stream()
                .map(room -> new ObjectRoom(room, dataProvider))
                .toList();

        return cachedRooms;
    }

    public int getCountRooms() {
        return getRooms().size();
    }
}
