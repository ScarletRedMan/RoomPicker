package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.dragonestia.picker.cp.model.Room;
import ru.dragonestia.picker.cp.model.User;
import ru.dragonestia.picker.cp.repository.UserRepository;
import ru.dragonestia.picker.cp.repository.impl.response.RoomUserListResponse;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class UserRepositoryImpl implements UserRepository {

    private final RestUtil rest;

    @Override
    public void linkWithRoom(Room room, Collection<User> users) {
        // TODO
    }

    @Override
    public void unlinkFromRoom(Room room, Collection<User> users) {
        // TODO
    }

    @Override
    public List<User> all(Room room) {
        try {
            var response = rest.get(URI.create("/nodes/%s/rooms/%s/users".formatted(room.getNodeId(), room.getId())),
                    RoomUserListResponse.class,
                    params -> {});

            return response.users();
        } catch (Exception ex) {
            log.throwing(ex);
            throw new Error("Internal error");
        }
    }
}
