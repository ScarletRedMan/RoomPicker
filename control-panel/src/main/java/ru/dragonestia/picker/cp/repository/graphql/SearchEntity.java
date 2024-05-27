package ru.dragonestia.picker.cp.repository.graphql;

import lombok.Getter;
import ru.dragonestia.picker.api.impl.util.GraphqlQuery;
import ru.dragonestia.picker.cp.repository.dto.EntityDTO;

import java.util.List;

@Getter
public class SearchEntity {

    private final static String QUERY = """
            query ($input: String!) {
              searchEntity(input: $input) {
                id
                countRooms
              }
            }
            """;

    private List<Entity> searchEntity;

    public static GraphqlQuery<SearchEntity> query(String input) {
        return new GraphqlQuery<>(QUERY, SearchEntity.class, params -> {
            params.put("input", input);
        });
    }

    @Getter
    public static class Entity implements EntityDTO {
        private String id;
        private int countRooms;
    }
}
