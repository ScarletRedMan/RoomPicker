package ru.dragonestia.picker.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.model.node.NodeDetails;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.response.type.RNode;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class DetailsExtractor {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RNode extract(Node node, Set<NodeDetails> details) {
        var response = node.toResponseObject();

        for (var detail: details) {
            if (detail == NodeDetails.PERSIST) {
                response.putDetail(NodeDetails.PERSIST, Boolean.toString(node.persist()));
                continue;
            }
        }

        return response;
    }

    public RRoom.Short extract(Room room, Set<RoomDetails> details) {
        var response = room.toShortResponseObject();

        for (var detail: details) {
            if (detail == RoomDetails.COUNT_USERS) {
                var users = Integer.toString(userRepository.usersOf(room).size());
                response.details().put(RoomDetails.COUNT_USERS, users);
                continue;
            }

            if (detail == RoomDetails.PERSIST) {
                response.details().put(RoomDetails.PERSIST, Boolean.toString(room.isPersist()));
                continue;
            }
        }

        return response;
    }

    public RUser extract(User user, Set<UserDetails> details) {
        var response = user.toResponseObject();

        for (var detail: details) {
            if (detail == UserDetails.COUNT_ROOMS) {
                response.putDetail(UserDetails.COUNT_ROOMS, Integer.toString(userRepository.findAllLinkedUserRooms(user).size()));
            }
        }

        return response;
    }
}
