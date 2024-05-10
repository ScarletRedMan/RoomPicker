package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.user.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    Collection<Room> getUserRooms(User user);

    void linkUsersWithRoom(Room room, Collection<User> users, boolean force) throws RoomAreFullException;

    void unlinkUsersFromRoom(Room room, Collection<User> users);

    Collection<User> getRoomUsers(Room room);

    List<ResponseUser> searchUsers(String input);

    ResponseUser getUserDetails(String userId);
}
