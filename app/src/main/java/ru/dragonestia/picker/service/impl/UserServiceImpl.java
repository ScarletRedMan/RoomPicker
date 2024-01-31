package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.api.repository.details.UserDetails;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.UserService;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Map<UserDetails, Function<User, String>> detailsMap = new HashMap<>();

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

    @Override
    public List<RUser> getRoomUsersWithDetailsResponse(Room room, Set<UserDetails> details) {
        var users = new LinkedList<RUser>();
        for (var user: getRoomUsers(room)) {
            var responseUser = user.toResponseObject();

            for (var detail: details) {
                if (detail == UserDetails.COUNT_ROOMS) {
                    responseUser.putDetail(UserDetails.COUNT_ROOMS, Integer.toString(getUserRooms(user).size()));
                }
            }

            users.add(responseUser);
        }
        return users;
    }

    @Override
    public List<RUser> searchUsers(String input, Set<UserDetails> details) {
        return userRepository.search(input).stream()
                .map(user -> {
                    var responseUser = user.toResponseObject();

                    for (var detail: details) {
                        if (detail == UserDetails.COUNT_ROOMS) {
                            responseUser.putDetail(UserDetails.COUNT_ROOMS, Integer.toString(getUserRooms(user).size()));
                        }
                    }

                    return responseUser;
                }).toList();
    }
}
