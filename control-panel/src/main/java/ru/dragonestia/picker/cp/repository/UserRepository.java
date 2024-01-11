package ru.dragonestia.picker.cp.repository;

import ru.dragonestia.picker.cp.model.Room;
import ru.dragonestia.picker.cp.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    void linkWithRoom(Room room, Collection<User> users, boolean force);

    void unlinkFromRoom(Room room, Collection<User> users);

    List<User> all(Room room);
}
