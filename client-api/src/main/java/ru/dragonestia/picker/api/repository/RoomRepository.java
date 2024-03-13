package ru.dragonestia.picker.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.room.RoomDefinition;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.repository.request.room.FindRoomById;
import ru.dragonestia.picker.api.repository.request.room.GetAllRooms;
import ru.dragonestia.picker.api.repository.request.room.RemoveRoomsByIds;
import ru.dragonestia.picker.api.repository.type.RoomPath;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    void saveRoom(@NotNull RoomDefinition definition);

    void removeRooms(@NotNull RemoveRoomsByIds request);

    void removeRoom(@NotNull IRoom room);

    @NotNull List<ShortResponseRoom> allRooms(@NotNull GetAllRooms request);

    @NotNull Optional<ResponseRoom> find(@NotNull FindRoomById request);

    void lockRoom(@NotNull RoomPath path, boolean value);
}
