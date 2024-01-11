package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.UserService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<Room> getUserRooms(User user) {
        return userRepository.findAllLinkedUserRooms(user);
    }

    @Override
    public int linkUsersWithRoom(Room room, Collection<User> users, boolean force) {
        userRepository.linkWithRoom(room, users, force);
        return userRepository.usersOf(room).size();
    }

    @Override
    public void unlinkUsersFromRoom(Room room, Collection<User> users) {
        userRepository.unlinkWithRoom(room, users);
    }

    @Override
    public List<User> getRoomUsers(Room room) {
        return userRepository.usersOf(room);
    }
}
