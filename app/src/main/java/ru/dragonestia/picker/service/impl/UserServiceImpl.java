package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.api.repository.details.UserDetails;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.DetailsExtractor;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DetailsExtractor detailsExtractor;
    private final Map<UserDetails, Function<User, String>> detailsMap = new HashMap<>();

    @Override
    public List<Room> getUserRooms(User user) {
        return userRepository.findAllLinkedUserRooms(user);
    }

    @Override
    public List<RRoom.Short> getUserRoomsWithDetails(User user, Set<RoomDetails> details) {
        var result = new LinkedList<RRoom.Short>();
        for (var room: getUserRooms(user)) {
            result.add(detailsExtractor.extract(room, details));
        }
        return result;
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
            users.add(detailsExtractor.extract(user, details));
        }
        return users;
    }

    @Override
    public List<RUser> searchUsers(String input, Set<UserDetails> details) {
        return userRepository.search(input).stream().map(user -> detailsExtractor.extract(user, details)).toList();
    }

    @Override
    public RUser getUserDetails(String userId, Set<UserDetails> details) {
        return detailsExtractor.extract(new User(userId), details);
    }
}
