package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.model.Room;
import ru.dragonestia.picker.api.model.User;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.response.RoomUserListResponse;

import java.util.Collection;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class UserRepositoryImpl implements UserRepository {

    private final RestUtil rest;

    @Override
    public void linkWithRoom(Room room, Collection<User> users, boolean force) throws NodeNotFoundException, RoomNotFoundException, RoomAreFullException {
        rest.query("/nodes/%s/rooms/%s/users".formatted(room.getNodeId(), room.getId()),
                HttpMethod.POST,
                params -> {
                    params.put("userIds", String.join(",", users.stream().map(User::getId).toList()));
                    params.put("force", Boolean.toString(force));
                });
    }

    @Override
    public void unlinkFromRoom(Room room, Collection<User> users) throws NodeNotFoundException, RoomNotFoundException {
        rest.query("/nodes/%s/rooms/%s/users".formatted(room.getNodeId(), room.getId()),
                HttpMethod.DELETE,
                params -> {
                    params.put("userIds", String.join(",", users.stream().map(User::getId).toList()));
                });
    }

    @Override
    public List<User> all(Room room) throws NodeNotFoundException, RoomNotFoundException {
        return rest.query("/nodes/%s/rooms/%s/users".formatted(room.getNodeId(), room.getId()),
                HttpMethod.GET,
                RoomUserListResponse.class,
                params -> {}).users();
    }
}
