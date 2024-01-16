package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.model.Room;
import ru.dragonestia.picker.api.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    void linkWithRoom(Room room, Collection<User> users, boolean force) throws NodeNotFoundException, RoomNotFoundException, RoomAreFullException;

    void unlinkFromRoom(Room room, Collection<User> users) throws NodeNotFoundException, RoomNotFoundException;

    List<User> all(Room room) throws NodeNotFoundException, RoomNotFoundException;
}
