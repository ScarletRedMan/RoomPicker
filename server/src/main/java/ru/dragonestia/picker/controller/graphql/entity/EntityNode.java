package ru.dragonestia.picker.controller.graphql.entity;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.controller.graphql.entity.type.DataProvider;
import ru.dragonestia.picker.model.node.Node;

import java.util.List;

@RequiredArgsConstructor
public class EntityNode {

    private final Node node;
    private final DataProvider dataProvider;
    private List<EntityRoom> cachedRooms = null;

    public String getId() {
        return node.getIdentifier();
    }

    public String getMethod() {
        return node.getPickingMethod().name();
    }

    public List<EntityRoom> getRooms() {
        if (cachedRooms != null) {
            return cachedRooms;
        }

        cachedRooms = dataProvider.roomService().all(node).stream()
                .map(room -> new EntityRoom(room, dataProvider))
                .toList();

        return cachedRooms;
    }

    public int getCountRooms() {
        return getRooms().size();
    }

    public boolean isPersist() {
        return node.isPersist();
    }
}
