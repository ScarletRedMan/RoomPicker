package ru.dragonestia.picker.controller.graphql.entity;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.controller.graphql.entity.type.DataProvider;
import ru.dragonestia.picker.model.Room;

import java.util.List;

@RequiredArgsConstructor
public class EntityRoom {

    private final Room room;
    private final DataProvider dataProvider;
    private List<EntityUser> cachedUsers = null;

    public String getId() {
        return room.getIdentifier();
    }

    public String getNodeId() {
        return room.getNodeIdentifier();
    }

    public EntityNode getNode() {
        return dataProvider.nodeService().find(room.getNodeIdentifier())
                .map(node -> new EntityNode(node, dataProvider))
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

    public List<EntityUser> getUsers() {
        if (cachedUsers != null) {
            return cachedUsers;
        }

        cachedUsers = dataProvider.userService().getRoomUsers(room).stream()
                .map(user -> new EntityUser(user, dataProvider))
                .toList();

        return cachedUsers;
    }

    public int getCountUsers() {
        return getUsers().size();
    }

    public boolean isPersist() {
        return room.isPersist();
    }
}
