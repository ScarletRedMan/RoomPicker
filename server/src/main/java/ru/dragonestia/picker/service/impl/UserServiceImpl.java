package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.DetailsExtractor;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DetailsExtractor detailsExtractor;

    @Override
    public List<Room> getUserRooms(User user) {
        return userRepository.findAllLinkedUserRooms(user);
    }

    @Override
    public List<ShortResponseRoom> getUserRoomsWithDetails(User user, Set<RoomDetails> details) {
        var result = new LinkedList<ShortResponseRoom>();
        for (var room: getUserRooms(user)) {
            result.add(detailsExtractor.extract(room, details));
        }
        return result;
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
    public List<User> getRoomUsers(Room room) {
        return userRepository.usersOf(room);
    }

    @Override
    public List<ResponseUser> getRoomUsersWithDetailsResponse(Room room, Set<UserDetails> details) {
        var users = new LinkedList<ResponseUser>();
        for (var user: getRoomUsers(room)) {
            users.add(detailsExtractor.extract(user, details));
        }
        return users;
    }

    @Override
    public List<ResponseUser> searchUsers(String input, Set<UserDetails> details) {
        return userRepository.search(input).stream().map(user -> detailsExtractor.extract(user, details)).toList();
    }

    @Override
    public ResponseUser getUserDetails(String userId, Set<UserDetails> details) {
        return detailsExtractor.extract(new User(UserIdentifier.of(userId)), details);
    }
}
