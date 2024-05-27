package ru.dragonestia.picker.cp.repository.graphql;

import lombok.Getter;
import ru.dragonestia.picker.api.impl.util.GraphqlQuery;
import ru.dragonestia.picker.cp.repository.dto.RoomDTO;

import java.util.List;

@Getter
public class AllRooms {

    private final static String QUERY = """
            query ($nodeId: String!) {
              allRooms(nodeId: $nodeId) {
                id
                instanceId
                slots
                locked
                countEntities
                persist
              }
            }
            """;

    private List<Room> allRooms;

    public static GraphqlQuery<AllRooms> query(String nodeId) {
        return new GraphqlQuery<>(QUERY, AllRooms.class, params -> {
            params.put("nodeId", nodeId);
        });
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
