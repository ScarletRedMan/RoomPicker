package ru.dragonestia.picker.cp.util;

import com.vaadin.flow.router.BeforeEnterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.InstanceNotFoundException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.account.IAccount;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.repository.query.node.FindNodeById;
import ru.dragonestia.picker.api.repository.query.room.FindRoomById;
import ru.dragonestia.picker.api.repository.query.user.FindUserById;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;

@Component
@RequiredArgsConstructor
public class RouteParamsExtractor {

    private final RoomPickerClient client;

    public INode extractNode(BeforeEnterEvent e) throws InstanceNotFoundException {
        var nodeId = NodeIdentifier.of(e.getRouteParameters().get("nodeId").orElseThrow(() -> new InstanceNotFoundException("null")));
        return client.getNodeRepository().findNodeById(FindNodeById.justFind(nodeId)).orElseThrow(() -> new InstanceNotFoundException(nodeId.getValue()));
    }

    public IRoom extractRoom(BeforeEnterEvent e, INode node) throws RoomNotFoundException {
        var nodeId = node.getIdentifierObject();
        var roomId = RoomIdentifier.of(e.getRouteParameters().get("roomId").orElseThrow(() -> new InstanceNotFoundException("null")));
        return client.getRoomRepository().find(FindRoomById.just(nodeId, roomId)).orElseThrow(() -> new InstanceNotFoundException(roomId.getValue()));
    }

    public IUser extractUser(BeforeEnterEvent e) {
        var userId = EntityIdentifier.of(e.getRouteParameters().get("userId").orElseThrow(RuntimeException::new));
        return client.getUserRepository().findUserById(FindUserById.withAllDetails(userId));
    }

    public IAccount extractAccount(BeforeEnterEvent e) {
        var accountId = e.getRouteParameters().get("accountId").orElseThrow(RuntimeException::new);
        return client.getAccountRepository().findAccountByUsername(accountId).orElseThrow(RuntimeException::new);
    }
}
