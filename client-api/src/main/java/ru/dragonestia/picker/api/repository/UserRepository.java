package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.api.model.user.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRepository {

    void linkWithRoom(ResponseRoom room, Collection<ResponseUser> users, boolean force);

    void unlinkFromRoom(ResponseRoom room, Collection<ResponseUser> users);

    List<ResponseUser> all(ResponseRoom room, Set<UserDetails> details);

    List<ResponseUser> search(String input, Set<UserDetails> details);

    ResponseUser find(String userId, Set<UserDetails> details);

    List<ShortResponseRoom> getLinkedRoomsWithUsers(ResponseUser user, Set<RoomDetails> roomDetails);
}
