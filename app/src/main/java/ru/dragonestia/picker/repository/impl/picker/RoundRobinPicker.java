package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.PickingMode;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.repository.impl.collection.QueuedLinkedList;

import java.util.Collection;

public class RoundRobinPicker implements RoomPicker {

    private final UserRepository userRepository;
    private final QueuedLinkedList<RoomWrapper> list = new QueuedLinkedList<>();

    public RoundRobinPicker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(Room room) {
        synchronized (list) {
            list.add(new RoomWrapper(room, () -> userRepository.usersOf(room).size()));
        }
    }

    @Override
    public void remove(Room room) {
        synchronized (list) {
            list.removeById(room.getId());
        }
    }

    @Override
    public Room pick(Collection<User> users) {
        int amount = users.size();
        RoomWrapper wrapper;

        synchronized (list) {
            try {
                while (!(wrapper = list.pick()).canAddUnits(amount));
            } catch (RuntimeException ex) {
                throw new RuntimeException("There are no rooms available");
            }
        }

        return wrapper.getItem();
    }

    @Override
    public PickingMode getPickingMode() {
        return PickingMode.ROUND_ROBIN;
    }
}
