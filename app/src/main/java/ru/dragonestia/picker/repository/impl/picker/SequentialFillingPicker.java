package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.api.model.type.PickingMode;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SequentialFillingPicker implements RoomPicker {

    private final UserRepository userRepository;
    private final Map<String, RoomWrapper> wrappers = new LinkedHashMap<>();

    public SequentialFillingPicker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(Room room) {
        synchronized (wrappers) {
            wrappers.put(room.getId(), new RoomWrapper(room, () -> userRepository.usersOf(room).size()));
        }
    }

    @Override
    public void remove(Room room) {
        synchronized (wrappers) {
            wrappers.remove(room.getId());
        }
    }

    @Override
    public Room pick(Collection<User> users) {
        int amount = users.size();

        synchronized (wrappers) {
            for (var wrapper: wrappers.values()) {
                if (!wrapper.canAddUnits(amount)) continue;

                return wrapper.getItem();
            }
        }

        throw new RuntimeException("There are no rooms available");
    }

    @Override
    public PickingMode getPickingMode() {
        return PickingMode.SEQUENTIAL_FILLING;
    }
}
