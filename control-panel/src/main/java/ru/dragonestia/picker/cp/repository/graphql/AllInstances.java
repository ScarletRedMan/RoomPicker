package ru.dragonestia.picker.cp.repository.graphql;

import lombok.Getter;
import ru.dragonestia.picker.api.impl.util.GraphqlQuery;
import ru.dragonestia.picker.api.model.instance.type.PickingMethod;
import ru.dragonestia.picker.cp.repository.dto.InstanceDTO;

import java.util.List;

@Getter
public class AllInstances {

    private final static String QUERY = """
            {
              allInstances {
                id
                method
                persist
                countRooms
              }
            }
            """;

    private List<Instance> allInstances;

    public static GraphqlQuery<AllInstances> query() {
        return new GraphqlQuery<>(QUERY, AllInstances.class, params -> {});
    }

    @Getter
    public static class Instance implements InstanceDTO {
        private String id;
        private PickingMethod method;
        private boolean persist;
        private int countRooms;
    }
}
