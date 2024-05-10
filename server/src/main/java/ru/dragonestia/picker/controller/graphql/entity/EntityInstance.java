package ru.dragonestia.picker.controller.graphql.entity;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.controller.graphql.entity.type.DataProvider;
import ru.dragonestia.picker.model.instance.Instance;

import java.util.List;

@RequiredArgsConstructor
public class EntityInstance {

    private final Instance instance;
    private final DataProvider dataProvider;
    private List<EntityRoom> cachedRooms = null;

    public String getId() {
        return instance.getIdentifier();
    }

    public String getMethod() {
        return instance.getPickingMethod().name();
    }

    public List<EntityRoom> getRooms() {
        if (cachedRooms != null) {
            return cachedRooms;
        }

        cachedRooms = dataProvider.roomService().all(instance).stream()
                .map(room -> new EntityRoom(room, dataProvider))
                .toList();

        return cachedRooms;
    }

    public int getCountRooms() {
        return getRooms().size();
    }

    public boolean isPersist() {
        return instance.isPersist();
    }
}
