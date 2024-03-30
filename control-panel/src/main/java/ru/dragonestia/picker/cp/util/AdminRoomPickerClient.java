package ru.dragonestia.picker.cp.util;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.UserRepository;

public class AdminRoomPickerClient extends RoomPickerClient {

    public AdminRoomPickerClient(@NotNull String url, @NotNull String username, @NotNull String password) {
        super(url, username, password);
    }

    @Override
    public @NotNull NodeRepository getNodeRepository() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull RoomRepository getRoomRepository() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull UserRepository getUserRepository() {
        throw new UnsupportedOperationException();
    }
}
