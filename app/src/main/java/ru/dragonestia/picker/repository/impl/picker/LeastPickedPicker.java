package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.repository.impl.collection.DynamicSortedMap;

import java.util.Collection;

public class LeastPickedPicker implements RoomPicker {

    private final UserRepository userRepository;
    private final DynamicSortedMap<RoomWrapper> map = new DynamicSortedMap<>();

    public LeastPickedPicker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(Room room) {
        synchronized (map) {
            map.put(new RoomWrapper(room, () -> userRepository.usersOf(room).size()));
        }
    }

    @Override
    public void remove(Room room) {
        synchronized (map) {
            map.removeById(room.getId());
        }
    }

    @Override
    public Room pick(Collection<User> users) {
        RoomWrapper wrapper;

        synchronized (map) {
            try {
                wrapper = map.getMinimum();

                if (!wrapper.canAddUnits(users.size())) throw new RuntimeException();
            } catch (RuntimeException ex) {
                throw new RuntimeException("There are no rooms available");
            }
        }

        return wrapper.getItem();
    }

    public void updateUsersAmount(Room room, int users) {
        synchronized (map) {
            map.updateItem(room.getId(), prevValue -> users);
        }
    }

    @Override
    public PickingMode getPickingMode() {
        return PickingMode.LEAST_PICKED;
    }
}
