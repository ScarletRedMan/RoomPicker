package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.api.repository.details.UserDetails;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserService {

    List<Room> getUserRooms(User user);

    int linkUsersWithRoom(Room room, Collection<User> users, boolean force) throws RoomAreFullException;

    void unlinkUsersFromRoom(Room room, Collection<User> users);

    List<User> getRoomUsers(Room room);

    List<RUser> getRoomUsersWithDetailsResponse(Room room, Set<UserDetails> details);
}
