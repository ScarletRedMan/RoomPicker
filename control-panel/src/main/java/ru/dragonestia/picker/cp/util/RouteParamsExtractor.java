package ru.dragonestia.picker.cp.util;

import com.vaadin.flow.router.BeforeEnterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.response.type.RNode;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.response.type.RUser;

@Component
@RequiredArgsConstructor
public class RouteParamsExtractor {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RNode extractNodeId(BeforeEnterEvent e) throws NodeNotFoundException {
        var nodeId = e.getRouteParameters().get("nodeId").orElseThrow(() -> new NodeNotFoundException("null"));
        return nodeRepository.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
    }

    public RRoom extractRoomId(BeforeEnterEvent e, RNode node) throws RoomNotFoundException {
        var roomId = e.getRouteParameters().get("roomId").orElseThrow(() -> new NodeNotFoundException("null"));
        return roomRepository.find(node, roomId).orElseThrow(() -> new NodeNotFoundException(roomId));
    }

    public RUser extractUserId(BeforeEnterEvent e) {
        var userId = e.getRouteParameters().get("userId").orElseThrow(RuntimeException::new);
        return userRepository.find(userId, UserRepository.ALL_DETAILS);
    }
}
