package ru.dragonestia.picker.controller.graphql;

import jakarta.validation.constraints.NotNull;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.controller.graphql.entity.EntityInstance;
import ru.dragonestia.picker.controller.graphql.entity.EntityRoom;
import ru.dragonestia.picker.controller.graphql.entity.EntityUser;
import ru.dragonestia.picker.controller.graphql.entity.type.DataProvider;
import ru.dragonestia.picker.model.user.User;
import ru.dragonestia.picker.service.InstanceService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.UserService;

import java.util.List;

@Controller
public class GraphqlController {

    private final InstanceService instanceService;
    private final RoomService roomService;
    private final UserService userService;
    private final DataProvider dataProvider;

    public GraphqlController(InstanceService instanceService, RoomService roomService, UserService userService) {
        this.instanceService = instanceService;
        this.roomService = roomService;
        this.userService = userService;
        dataProvider = new DataProvider(instanceService, roomService, userService);
    }

    @QueryMapping
    List<EntityInstance> allInstances() {
        return instanceService.all().stream()
                .map(node -> new EntityInstance(node, dataProvider))
                .toList();
    }

    @QueryMapping
    EntityInstance instanceById(@Argument String id) {
        return instanceService.find(id)
                .map(node -> new EntityInstance(node, dataProvider))
                .orElse(null);
    }

    @QueryMapping
    List<EntityRoom> allRooms(@NotNull String nodeId) {
        var node = instanceService.find(nodeId).orElse(null);
        if (node == null) return null;

        return roomService.all(node).stream()
                .map(room -> new EntityRoom(room, dataProvider))
                .toList();
    }

    @QueryMapping
    EntityRoom roomById(@Argument String nodeId, @NotNull String roomId) {
        var node = instanceService.find(nodeId).orElse(null);
        if (node == null) return null;

        return roomService.find(node, roomId)
                .map(room -> new EntityRoom(room, dataProvider))
                .orElse(null);
    }

    @QueryMapping
    EntityUser userById(@Argument String id) {
        return new EntityUser(new User(UserIdentifier.of(id)), dataProvider);
    }

    @QueryMapping
    List<EntityUser> searchUser(@Argument String input) {
        return userService.searchUsers(input).stream()
                .map(user -> new EntityUser(new User(user.getIdentifierObject()), dataProvider))
                .toList();
    }
}
