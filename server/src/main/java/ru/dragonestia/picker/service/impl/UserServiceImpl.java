package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.UserService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<Room> getUserRooms(User user) {
        return userRepository.findAllLinkedUserRooms(user);
    }

    @Override
    public void linkUsersWithRoom(Room room, Collection<User> users, boolean force) {
        userRepository.linkWithRoom(room, users, force);
    }

    @Override
    public void unlinkUsersFromRoom(Room room, Collection<User> users) {
        userRepository.unlinkWithRoom(room, users);
    }

    @Override
    public Collection<User> getRoomUsers(Room room) {
        return userRepository.usersOf(room);
    }

    @Override
    public List<ResponseUser> searchUsers(String input) {
        return userRepository.search(input).stream().map(User::toResponseObject).toList();
    }

    @Override
    public ResponseUser getUserDetails(String userId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
