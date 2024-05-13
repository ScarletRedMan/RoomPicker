package ru.dragonestia.picker.cp.util;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.repository.InstanceRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.EntityRepository;

public class AdminRoomPickerClient extends RoomPickerClient {

    public AdminRoomPickerClient(@NotNull String url, @NotNull String username, @NotNull String password) {
        super(url, username, password);
    }

    @Override
    public @NotNull InstanceRepository getNodeRepository() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull RoomRepository getRoomRepository() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull EntityRepository getUserRepository() {
        throw new UnsupportedOperationException();
    }
}
