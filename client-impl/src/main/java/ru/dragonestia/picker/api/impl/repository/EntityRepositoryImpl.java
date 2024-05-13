package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.EnumUtils;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.model.user.ResponseUser;
import ru.dragonestia.picker.api.repository.EntityRepository;
import ru.dragonestia.picker.api.repository.query.user.*;
import ru.dragonestia.picker.api.repository.response.LinkedRoomsWithUserResponse;
import ru.dragonestia.picker.api.repository.response.RoomUserListResponse;
import ru.dragonestia.picker.api.repository.response.SearchUserResponse;
import ru.dragonestia.picker.api.repository.response.UserDetailsResponse;

import java.util.List;

public class EntityRepositoryImpl implements EntityRepository {

    private final RestTemplate rest;

    @Internal
    public EntityRepositoryImpl(RoomPickerClient client) {
        rest = client.getRestTemplate();
    }

    @Override
    public void linkUsersWithRoom(@NotNull LinkUsersWithRoom request) {
        rest.query("/nodes/%s/rooms/%s/users".formatted(request.getNodeId(), request.getRoomId()),
                HttpMethod.POST,
                params -> {
                    params.put("userIds", String.join(",", request.getUsers()));
                    params.put("force", Boolean.toString(request.ignoreSlotLimitation()));
                });
    }

    @Override
    public void unlinkUsersFromRoom(@NotNull UnlinkUsersFromRoom request) {
        rest.query("/nodes/%s/rooms/%s/users".formatted(request.getNodeId(), request.getRoomId()),
                HttpMethod.DELETE,
                params -> {
                    params.put("userIds", String.join(",", request.getUsers()));
                });
    }

    @Override
    public @NotNull List<ResponseUser> getAllUsersFormRoom(@NotNull GetAllUsersFromRoom request) {
        return rest.query("/nodes/%s/rooms/%s/users".formatted(request.getNodeId(), request.getRoomId()),
                HttpMethod.GET,
                RoomUserListResponse.class,
                params -> {
                    var detailsStr = String.join(",", EnumUtils.enumSetToString(request.getDetails()));

                    params.put("requiredDetails", detailsStr);
                }).users();
    }

    @Override
    public @NotNull List<ResponseUser> searchUsers(@NotNull SearchUsers request) {
        return rest.query("/users/search",
                HttpMethod.GET,
                SearchUserResponse.class,
                params -> {
                    params.put("requiredDetails", EnumUtils.enumSetToString(request.getDetails()));
                    params.put("input", request.getSearchInput());
                }).users();
    }

    @Override
    public @NotNull ResponseUser findUserById(@NotNull FindUserById request) {
        return rest.query("/users/" + request.getUserId(),
                HttpMethod.GET,
                UserDetailsResponse.class,
                params -> {
                    params.put("requiredDetails", EnumUtils.enumSetToString(request.getDetails()));
                }).user();
    }

    @Override
    public @NotNull List<ShortResponseRoom> findRoomsLinkedWithUser(@NotNull FindRoomsLinkedWithUser request) {
        return rest.query("/users/" + request.getUserId() + "/rooms",
                HttpMethod.GET,
                LinkedRoomsWithUserResponse.class,
                params -> {
                    params.put("requiredDetails", EnumUtils.enumSetToString(request.getDetails()));
                }).rooms();
    }
}
