package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.Collection;

public class LeastPickedPicker implements Picker<Room, User> {

    private final UserRepository userRepository;

    public LeastPickedPicker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(Room room) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Room room) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Room pick(Collection<User> users) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
