package ru.dragonestia.picker.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.api.repository.query.user.*;

import java.util.List;

public interface UserRepository {

    void linkUsersWithRoom(@NotNull LinkUsersWithRoom request);

    void unlinkUsersFromRoom(@NotNull UnlinkUsersFromRoom request);

    @NotNull List<ResponseUser> getAllUsersFormRoom(@NotNull GetAllUsersFromRoom request);

    @NotNull List<ResponseUser> searchUsers(@NotNull SearchUsers request);

    @NotNull ResponseUser findUserById(@NotNull FindUserById request);

    @NotNull List<ShortResponseRoom> findRoomsLinkedWithUser(@NotNull FindRoomsLinkedWithUser request);
}
