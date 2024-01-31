package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.repository.response.SearchUserResponse;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.details.UserDetails;
import ru.dragonestia.picker.api.repository.response.RoomUserListResponse;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class UserRepositoryImpl implements UserRepository {

    private final RestUtil rest;

    @Override
    public void linkWithRoom(RRoom room, Collection<RUser> users, boolean force) throws NodeNotFoundException, RoomNotFoundException, RoomAreFullException {
        rest.query("/nodes/%s/rooms/%s/users".formatted(room.getNodeId(), room.getId()),
                HttpMethod.POST,
                params -> {
                    params.put("userIds", String.join(",", users.stream().map(RUser::getId).toList()));
                    params.put("force", Boolean.toString(force));
                });
    }

    @Override
    public void unlinkFromRoom(RRoom room, Collection<RUser> users) throws NodeNotFoundException, RoomNotFoundException {
        rest.query("/nodes/%s/rooms/%s/users".formatted(room.getNodeId(), room.getId()),
                HttpMethod.DELETE,
                params -> {
                    params.put("userIds", String.join(",", users.stream().map(RUser::getId).toList()));
                });
    }

    @Override
    public List<RUser> all(RRoom room, Set<UserDetails> details) throws NodeNotFoundException, RoomNotFoundException {
        return rest.query("/nodes/%s/rooms/%s/users".formatted(room.getNodeId(), room.getId()),
                HttpMethod.GET,
                RoomUserListResponse.class,
                params -> {
                    var detailsStr = String.join(",", details.stream().map(Enum::toString).toList());

                    params.put("requiredDetails", detailsStr);
                }).users();
    }

    @Override
    public List<RUser> search(String input, Set<UserDetails> details) {
        return rest.query("/users/search",
                HttpMethod.GET,
                SearchUserResponse.class,
                params -> {
                    var detailsStr = String.join(",", details.stream().map(Enum::toString).toList());

                    params.put("requiredDetails", detailsStr);
                    params.put("input", input);
                }).users();
    }
}
