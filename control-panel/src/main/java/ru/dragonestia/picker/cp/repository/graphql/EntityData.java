package ru.dragonestia.picker.cp.repository.graphql;

import lombok.Getter;
import ru.dragonestia.picker.api.impl.util.GraphqlQuery;
import ru.dragonestia.picker.cp.repository.dto.EntityDTO;

@Getter
public class EntityData {

    private final static String QUERY = """
            query ($entityId: String!) {
              entityById(id: $entityId) {
                id
                countRooms
              }
            }
            """;

    private Entity entityById;

    public static GraphqlQuery<EntityData> query(String entityId) {
        return new GraphqlQuery<>(QUERY, EntityData.class, params -> {
            params.put("entityId", entityId);
        });
    }

    @Getter
    public static class Entity implements EntityDTO {
        private String id;
        private int countRooms;
    }
}
