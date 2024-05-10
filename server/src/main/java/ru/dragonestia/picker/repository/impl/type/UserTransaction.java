package ru.dragonestia.picker.repository.impl.type;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.user.User;

import java.util.Collection;
import java.util.function.Consumer;

public record UserTransaction(@NotNull Room room, Collection<User> target) {

    public interface Listener extends Consumer<UserTransaction> {}
}
