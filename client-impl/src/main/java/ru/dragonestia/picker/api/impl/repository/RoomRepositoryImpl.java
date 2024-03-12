package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.room.RoomDefinition;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.request.room.FindRoomById;
import ru.dragonestia.picker.api.repository.request.room.GetAllRooms;
import ru.dragonestia.picker.api.repository.request.room.RemoveRoomsByIds;
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
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void removeRooms(@NotNull RemoveRoomsByIds request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull List<ShortResponseRoom> allRooms(@NotNull GetAllRooms request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Optional<ResponseRoom> find(@NotNull FindRoomById request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void lockRoom(@NotNull RoomPath path, boolean value) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
