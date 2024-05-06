package ru.dragonestia.picker.controller.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.dragonestia.picker.controller.graphql.entity.EntityNode;
import ru.dragonestia.picker.controller.graphql.entity.type.DataProvider;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.UserService;

import java.util.List;

@Controller
public class GraphqlController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final UserService userService;
    private final DataProvider dataProvider;

    public GraphqlController(NodeService nodeService, RoomService roomService, UserService userService) {
        this.nodeService = nodeService;
        this.roomService = roomService;
        this.userService = userService;
        dataProvider = new DataProvider(nodeService, roomService, userService);
    }

    @QueryMapping
    List<EntityNode> allNodes() {
        return nodeService.all().stream()
                .map(node -> new EntityNode(node, dataProvider))
                .toList();
    }
}
