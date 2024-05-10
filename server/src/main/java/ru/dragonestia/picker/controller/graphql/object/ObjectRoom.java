package ru.dragonestia.picker.controller.graphql.object;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.controller.graphql.object.type.DataProvider;
import ru.dragonestia.picker.model.room.Room;

import java.util.List;

@RequiredArgsConstructor
public class ObjectRoom {

    private final Room room;
    private final DataProvider dataProvider;
    private List<ObjectEntity> cachedUsers = null;

    public String getId() {
        return room.getIdentifier();
    }

    public String getInstanceId() {
        return room.getInstanceIdentifier();
    }

    public ObjectInstance getInstance() {
        return dataProvider.instanceService().find(room.getInstanceIdentifier())
                .map(node -> new ObjectInstance(node, dataProvider))
                .orElseThrow();
    }

    public int getSlots() {
        return room.getMaxSlots();
    }

    public String getPayload() {
        return room.getPayload();
    }

    public boolean isLocked() {
        return room.isLocked();
    }

    public List<ObjectEntity> getEntities() {
        if (cachedUsers != null) {
            return cachedUsers;
        }

        cachedUsers = dataProvider.entityService().getRoomEntities(room).stream()
                .map(user -> new ObjectEntity(user, dataProvider))
                .toList();

        return cachedUsers;
    }

    public int getCountEntities() {
        return getEntities().size();
    }

    public boolean isPersist() {
        return room.isPersist();
    }
}
