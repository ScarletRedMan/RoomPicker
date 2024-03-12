package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.request.user.*;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final RestTemplate rest;

    @Internal
    public UserRepositoryImpl(RoomPickerClient client) {
        rest = client.getRestTemplate();
    }

    @Override
    public void linkUsersWithRoom(@NotNull LinkUsersWithRoom request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void unlinkUsersFromRoom(@NotNull UnlinkUsersFromRoom request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull List<ResponseUser> getAllUsersFormRoom(@NotNull GetAllUsersFromRoom request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull List<ResponseUser> searchUsers(@NotNull SearchUsers request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull ResponseUser findUserById(@NotNull FindUserById request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull List<ShortResponseRoom> findRoomsLinkedWithUser(@NotNull FindRoomsLinkedWithUser request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
