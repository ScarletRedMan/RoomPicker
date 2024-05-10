package ru.dragonestia.picker.model.room.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.room.RoomId;

@Component
@RequiredArgsConstructor
public class RoomFactory {

    public Room create(RoomId identifier, Instance instance, int slots, String payload, boolean persist) {
        return new Room(identifier, instance, slots, payload, persist);
    }
}
