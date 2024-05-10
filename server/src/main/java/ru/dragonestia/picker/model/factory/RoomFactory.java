package ru.dragonestia.picker.model.factory;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;

@Component
@RequiredArgsConstructor
public class RoomFactory {

    @Contract("_, _, _, _, _ -> new")
    public @NotNull Room create(@NotNull RoomIdentifier identifier, @NotNull Instance instance, int slots, @NotNull String payload, boolean persist) {
        return new Room(identifier, instance, slots, payload, persist);
    }
}
