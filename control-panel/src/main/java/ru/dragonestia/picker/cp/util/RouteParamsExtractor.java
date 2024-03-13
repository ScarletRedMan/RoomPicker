package ru.dragonestia.picker.cp.util;

import com.vaadin.flow.router.BeforeEnterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.repository.request.node.FindNodeById;
import ru.dragonestia.picker.api.repository.request.room.FindRoomById;
import ru.dragonestia.picker.api.repository.request.user.FindUserById;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

@Component
@RequiredArgsConstructor
public class RouteParamsExtractor {

    private final RoomPickerClient client;

    public INode extractNode(BeforeEnterEvent e) throws NodeNotFoundException {
        var nodeId = NodeIdentifier.of(e.getRouteParameters().get("nodeId").orElseThrow(() -> new NodeNotFoundException("null")));
        return client.getNodeRepository().findNodeById(FindNodeById.justFind(nodeId)).orElseThrow(() -> new NodeNotFoundException(nodeId.getValue()));
    }

    public IRoom extractRoom(BeforeEnterEvent e, INode node) throws RoomNotFoundException {
        var nodeId = node.getIdentifierObject();
        var roomId = RoomIdentifier.of(e.getRouteParameters().get("roomId").orElseThrow(() -> new NodeNotFoundException("null")));
        return client.getRoomRepository().find(FindRoomById.just(nodeId, roomId)).orElseThrow(() -> new NodeNotFoundException(roomId.getValue()));
    }

    public IUser extractUser(BeforeEnterEvent e) {
        var userId = UserIdentifier.of(e.getRouteParameters().get("userId").orElseThrow(RuntimeException::new));
        return client.getUserRepository().findUserById(FindUserById.withAllDetails(userId));
    }
}
