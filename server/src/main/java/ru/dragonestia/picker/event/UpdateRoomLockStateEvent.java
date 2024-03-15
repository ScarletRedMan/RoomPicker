package ru.dragonestia.picker.event;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.model.Room;

import java.util.function.Consumer;

public record UpdateRoomLockStateEvent(@NotNull Room room) {

    public interface Listener extends Consumer<UpdateRoomLockStateEvent> {}
}
