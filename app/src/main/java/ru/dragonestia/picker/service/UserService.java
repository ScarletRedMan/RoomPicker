package ru.dragonestia.picker.service;

import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    List<Room> getUserRooms(User user);

    void linkUsersWithRoom(Room room, Collection<User> users, boolean force);

    void unlinkUsersFromRoom(Room room, Collection<User> users);

    List<User> getRoomUsers(Room room);
}
