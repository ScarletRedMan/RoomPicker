package ru.dragonestia.picker.model.factory;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.model.node.Node;
import ru.dragonestia.picker.model.room.Room;

@Component
@RequiredArgsConstructor
public class RoomFactory {

    @Contract("_, _, _, _, _ -> new")
    public @NotNull Room create(@NotNull RoomIdentifier identifier, @NotNull Node node, int slots, @NotNull String payload, boolean persist) {
        return new Room(identifier, node, slots, payload, persist);
    }
}
