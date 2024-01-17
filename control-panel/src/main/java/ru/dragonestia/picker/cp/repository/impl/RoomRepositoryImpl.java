package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.response.type.RNode;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.response.RoomInfoResponse;
import ru.dragonestia.picker.api.repository.response.RoomListResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class RoomRepositoryImpl implements RoomRepository {

    private final RestUtil rest;

    @Override
    public void register(RRoom room) throws NodeNotFoundException, InvalidRoomIdentifierException, RoomAlreadyExistException {
        rest.query("/nodes/" + room.getNodeId() + "/rooms", HttpMethod.POST, params -> {
            params.put("roomId", room.getId());
            params.put("slots", Integer.toString(room.getSlots()));
            params.put("payload", room.getPayload());
            params.put("locked", Boolean.toString(room.isLocked()));
        });
    }

    @Override
    public void remove(RRoom room) throws NodeNotFoundException {
        rest.query("/nodes/" + room.getNodeId() + "/rooms/" + room.getId(), HttpMethod.DELETE, params -> {});
    }

    @Override
    public void remove(RNode node, RRoom.Short room) throws NodeNotFoundException {
        rest.query("/nodes/" + node.getId() + "/rooms/" + room.id(), HttpMethod.DELETE, params -> {});
    }

    @Override
    public List<RRoom.Short> all(RNode node, Set<RoomDetails> details) throws NodeNotFoundException {
        return rest.query("/nodes/" + node.getId() + "/rooms", HttpMethod.GET, RoomListResponse.class, params -> {
            params.put("requiredDetails", String.join(",", details.stream().map(Enum::toString).toList()));
        }).rooms();
    }

    @Override
    public Optional<RRoom> find(RNode node, String roomId) throws NodeNotFoundException {
        try {
            var response = rest.query("/nodes/" + node.getId() + "/rooms/" + roomId, HttpMethod.GET, RoomInfoResponse.class, map -> {});
            return Optional.of(response.room());
        } catch (RoomNotFoundException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void lock(RRoom room, boolean value) throws NodeNotFoundException, RoomNotFoundException {
        rest.query("/nodes/%s/rooms/%s/lock".formatted(room.getNodeId(), room.getId()), HttpMethod.PUT, params -> {
            params.put("newState", Boolean.toString(value));
        });
    }
}
