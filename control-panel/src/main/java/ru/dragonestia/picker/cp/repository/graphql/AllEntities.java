package ru.dragonestia.picker.cp.repository.graphql;

import lombok.Getter;
import ru.dragonestia.picker.api.impl.util.GraphqlQuery;
import ru.dragonestia.picker.cp.repository.dto.EntityDTO;

import java.util.List;

@Getter
public class AllEntities {

    private final static String QUERY = """
            query ($instanceId: String!, $roomId: String!) {
              roomById(nodeId: $instanceId, roomId: $roomId) {
                entities {
                  id
                  countRooms
                }
              }
            }
            """;

    private Room roomById;

    public static GraphqlQuery<AllEntities> query(String instanceId, String roomId) {
        return new GraphqlQuery<>(QUERY, AllEntities.class, params -> {
            params.put("instanceId", instanceId);
            params.put("roomId", roomId);
        });
    }

    @Getter
    public static class Room {
        private List<Entity> entities;
    }

    @Getter
    public static class Entity implements EntityDTO {
        private String id;
        private int countRooms;
    }
}
