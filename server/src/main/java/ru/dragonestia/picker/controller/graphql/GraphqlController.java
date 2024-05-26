package ru.dragonestia.picker.controller.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.dragonestia.picker.controller.graphql.object.ObjectInstance;
import ru.dragonestia.picker.controller.graphql.object.ObjectRoom;
import ru.dragonestia.picker.controller.graphql.object.ObjectEntity;
import ru.dragonestia.picker.controller.graphql.object.type.DataProvider;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.service.InstanceService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.EntityService;

import java.util.List;

@Controller
public class GraphqlController {

    private final InstanceService instanceService;
    private final RoomService roomService;
    private final EntityService entityService;
    private final DataProvider dataProvider;

    public GraphqlController(InstanceService instanceService, RoomService roomService, EntityService entityService) {
        this.instanceService = instanceService;
        this.roomService = roomService;
        this.entityService = entityService;
        dataProvider = new DataProvider(instanceService, roomService, entityService);
    }

    @QueryMapping
    List<ObjectInstance> allInstances() {
        return instanceService.all().stream()
                .map(node -> new ObjectInstance(node, dataProvider))
                .toList();
    }

    @QueryMapping
    ObjectInstance instanceById(@Argument String id) {
        return instanceService.find(InstanceId.of(id))
                .map(node -> new ObjectInstance(node, dataProvider))
                .orElse(null);
    }

    @QueryMapping
    List<ObjectRoom> allRooms(@Argument String nodeId) {
        var node = instanceService.find(InstanceId.of(nodeId)).orElse(null);
        if (node == null) return null;

        return roomService.all(node.getId()).stream()
                .map(room -> new ObjectRoom(room, dataProvider))
                .toList();
    }

    @QueryMapping
    ObjectRoom roomById(@Argument String nodeId, @Argument String roomId) {
        var node = instanceService.find(InstanceId.of(nodeId)).orElse(null);
        if (node == null) return null;

        return roomService.find(node.getId(), RoomId.of(roomId))
                .map(room -> new ObjectRoom(room, dataProvider))
                .orElse(null);
    }

    @QueryMapping
    ObjectEntity entityById(@Argument String id) {
        return new ObjectEntity(new Entity(EntityId.of(id)), dataProvider);
    }

    @QueryMapping
    List<ObjectEntity> searchEntity(@Argument String input) {
        return entityService.searchEntities(EntityId.of(input)).stream()
                .map(user -> new ObjectEntity(new Entity(user.getId()), dataProvider))
                .toList();
    }
}
