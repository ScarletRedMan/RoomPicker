package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserRepository {

    Map<User, Boolean> linkWithRoom(Room room, Collection<User> users, boolean force);

    int unlinkWithRoom(Room room, Collection<User> users);

    List<Room> findAllLinkedUserRooms(User user);

    void onRemoveRoom(Room room);

    List<User> usersOf(Room room);
}
