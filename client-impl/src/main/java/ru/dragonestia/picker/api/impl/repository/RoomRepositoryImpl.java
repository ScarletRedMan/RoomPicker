package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.EnumUtils;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.room.RoomDefinition;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.query.room.FindRoomById;
import ru.dragonestia.picker.api.repository.query.room.GetAllRooms;
import ru.dragonestia.picker.api.repository.query.room.RemoveRoomsByIds;
import ru.dragonestia.picker.api.repository.response.RoomInfoResponse;
import ru.dragonestia.picker.api.repository.response.RoomListResponse;
import ru.dragonestia.picker.api.repository.type.RoomPath;

import java.util.List;
import java.util.Optional;

public class RoomRepositoryImpl implements RoomRepository {

    private final RestTemplate rest;

    @Internal
    public RoomRepositoryImpl(RoomPickerClient client) {
        rest = client.getRestTemplate();
    }

    @Override
    public void saveRoom(@NotNull RoomDefinition definition) {
        rest.query("/nodes/" + definition.getInstanceIdentifier() + "/rooms", HttpMethod.POST, params -> {
            params.put("roomId", definition.getIdentifier());
            params.put("slots", Integer.toString(definition.getMaxSlots()));
            params.put("payload", definition.getPayload());
            params.put("locked", Boolean.toString(definition.isLocked()));
            params.put("persist", Boolean.toString(definition.isPersist()));
        });
    }

    @Override
    public void removeRooms(@NotNull RemoveRoomsByIds request) {
        if (request.getRoomsIds().isEmpty()) return;

        rest.query("/nodes/" + request.getNodeId() + "/rooms", HttpMethod.DELETE, params -> {
            params.put("toDelete", String.join(",", request.getRoomsIds()));
        });
    }

    @Override
    public void removeRoom(@NotNull IRoom room) {
        rest.query(
                "/nodes/%s/rooms/%s".formatted(room.getInstanceIdentifier(), room.getIdentifier()),
                HttpMethod.DELETE,
                params -> {}
        );
    }

    @Override
    public @NotNull List<ShortResponseRoom> allRooms(@NotNull GetAllRooms request) {
        return rest.query("/nodes/" + request.getNodeId() + "/rooms", HttpMethod.GET, RoomListResponse.class, params -> {
            params.put("requiredDetails", EnumUtils.enumSetToString(request.getDetails()));
        }).rooms();
    }

    @Override
    public @NotNull Optional<ResponseRoom> find(@NotNull FindRoomById request) {
        try {
            var response = rest.query("/nodes/%s/rooms/%s".formatted(request.getNodeId(), request.getId()), HttpMethod.GET, RoomInfoResponse.class, params -> {
                params.put("requiredDetails", EnumUtils.enumSetToString(request.getDetails()));
            });
            return Optional.of(response.room());
        } catch (RoomNotFoundException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void lockRoom(@NotNull RoomPath path, boolean value) {
        rest.query("/nodes/%s/rooms/%s/lock".formatted(path.getNodeId(), path.getRoomId()), HttpMethod.PUT, params -> {
            params.put("newState", Boolean.toString(value));
        });
    }
}
