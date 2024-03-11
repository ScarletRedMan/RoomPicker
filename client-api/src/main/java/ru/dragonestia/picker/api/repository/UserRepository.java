package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRepository {

    Set<UserDetails> ALL_DETAILS = Set.of(UserDetails.COUNT_ROOMS);

    void linkWithRoom(RRoom room, Collection<RUser> users, boolean force) throws NodeNotFoundException, RoomNotFoundException, RoomAreFullException;

    void unlinkFromRoom(RRoom room, Collection<RUser> users) throws NodeNotFoundException, RoomNotFoundException;

    default List<RUser> all(RRoom room) throws NodeNotFoundException, RoomNotFoundException {
        return all(room, Set.of());
    }

    List<RUser> all(RRoom room, Set<UserDetails> details) throws NodeNotFoundException, RoomNotFoundException;

    List<RUser> search(String input, Set<UserDetails> details);

    RUser find(String userId, Set<UserDetails> details);

    List<RRoom.Short> getLinkedRoomsWithUsers(RUser user, Set<RoomDetails> roomDetails);
}
