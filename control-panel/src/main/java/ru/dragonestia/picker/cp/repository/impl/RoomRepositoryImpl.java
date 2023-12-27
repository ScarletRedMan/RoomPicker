package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;
import ru.dragonestia.picker.cp.model.Room;
import ru.dragonestia.picker.cp.model.Node;
import ru.dragonestia.picker.cp.model.dto.RoomDTO;
import ru.dragonestia.picker.cp.repository.RoomRepository;
import ru.dragonestia.picker.cp.repository.impl.response.RoomInfoResponse;
import ru.dragonestia.picker.cp.repository.impl.response.RoomListResponse;
import ru.dragonestia.picker.cp.repository.impl.response.RoomRegisterResponse;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class RoomRepositoryImpl implements RoomRepository {

    private final RestUtil rest;

    @Override
    public List<RoomDTO> all(Node node) {
        var entity = rest.getEntity(URI.create("/nodes/" + node.id() + "/rooms"),
                RoomListResponse.class);

        if (entity.getStatusCode().value() == 404) {
            throw new Error("Node with identifier '" + node.id() + "' does not exists'");
        }

        if (!entity.hasBody()) {
            throw new Error("Room list did not present");
        }

        return Objects.requireNonNull(entity.getBody()).rooms();
    }

    @Override
    public void register(Room room) {
        try {
            var response = rest.post(URI.create("/nodes/" + room.getNodeId() + "/rooms"),
                    RoomRegisterResponse.class,
                    params -> {
                        params.put("roomId", room.getId());
                        params.put("slots", Integer.toString(room.getSlots().slots()));
                        params.put("payload", room.getPayload());
                        params.put("locked", Boolean.toString(room.isLocked()));
                    });

            if (response.success()) return;
            throw new Error(response.message());
        } catch (HttpClientErrorException ex) {
            var response = ex.getResponseBodyAs(RoomRegisterResponse.class);

            if (response != null) {
                throw new Error(response.message());
            }

            log.throwing(ex);
            throw new Error("Internal error. Check logs");
        }
    }

    @Override
    public void remove(Room room) {
        rest.delete(URI.create("/nodes/" + room.getNodeId() + "/rooms/" + room.getId()), params -> {});
    }

    @Override
    public void remove(Node node, RoomDTO room) {
        rest.delete(URI.create("/nodes/" + node.id() + "/rooms/" + room.id()), params -> {});
    }

    @Override
    public Optional<Room> find(Node node, String roomId) {
        try {
            var response = rest.get(URI.create("/nodes/" + node.id() + "/rooms/" + roomId), RoomInfoResponse.class, map -> {});
            return Optional.of(response.room());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public void lock(Room room, boolean value) {
        try {
            rest.post(URI.create(room.createApiURI() + "/lock"), Boolean.class, params -> {
                params.put("newState", Boolean.toString(value));
            });
        } catch (Exception ex) {
            log.throwing(ex);
            throw new Error("Error when changing room locked state");
        }
    }
}
