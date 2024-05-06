package ru.dragonestia.picker.controller.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.dragonestia.picker.controller.graphql.entity.EntityNode;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphqlController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final UserService userService;

    @QueryMapping
    List<EntityNode> allNodes() {
        return nodeService.all().stream()
                .map(EntityNode::new)
                .toList();
    }
}
