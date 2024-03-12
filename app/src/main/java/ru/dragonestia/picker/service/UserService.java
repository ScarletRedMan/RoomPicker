package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserService {

    List<Room> getUserRooms(User user);

    List<ShortResponseRoom> getUserRoomsWithDetails(User user, Set<RoomDetails> details);

    int linkUsersWithRoom(Room room, Collection<User> users, boolean force) throws RoomAreFullException;

    void unlinkUsersFromRoom(Room room, Collection<User> users);

    List<User> getRoomUsers(Room room);

    List<ResponseUser> getRoomUsersWithDetailsResponse(Room room, Set<UserDetails> details);

    List<ResponseUser> searchUsers(String input, Set<UserDetails> details);

    ResponseUser getUserDetails(String userId, Set<UserDetails> details);
}
