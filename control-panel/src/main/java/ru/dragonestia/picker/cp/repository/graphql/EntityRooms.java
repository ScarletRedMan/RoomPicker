package ru.dragonestia.picker.cp.repository.graphql;

import lombok.Getter;
import ru.dragonestia.picker.api.impl.util.GraphqlQuery;
import ru.dragonestia.picker.cp.repository.dto.RoomDTO;

import java.util.List;

@Getter
public class EntityRooms {

    private final static String QUERY = """
            query ($entityId: String!) {
              entityById(id: $entityId) {
                rooms {
                  id
                  instanceId
                  countEntities
                  locked
                  persist
                  slots
                }
              }
            }
            """;

    private Entity entityById;

    public static GraphqlQuery<EntityRooms> query(String entityId) {
        return new GraphqlQuery<>(QUERY, EntityRooms.class, params -> {
            params.put("entityId", entityId);
        });
    }

    @Getter
    public static class Entity {
        private List<Room> rooms;
    }

    @Getter
    public static class Room implements RoomDTO {
        private String id;
        private String instanceId;
        private int slots;
        private boolean locked;
        private int countEntities;
        private boolean persist;
    }
}
