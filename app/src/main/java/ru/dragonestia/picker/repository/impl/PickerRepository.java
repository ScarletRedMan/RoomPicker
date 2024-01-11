package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.PickingMode;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.repository.impl.picker.LeastPickedPicker;
import ru.dragonestia.picker.repository.impl.picker.Picker;
import ru.dragonestia.picker.repository.impl.picker.RoundRobinPicker;
import ru.dragonestia.picker.repository.impl.picker.SequentialFillingPicker;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class PickerRepository {

    private final UserRepository userRepository;
    private final Map<String, Picker<Room, User>> pickers = new ConcurrentHashMap<>();

    public void create(String nodeId, PickingMode mode) {
        pickers.put(nodeId, of(mode));
    }

    public void remove(String nodeId) {
        pickers.remove(nodeId);
    }

    public Picker<Room, User> find(String nodeId) {
        return pickers.get(nodeId);
    }

    public Room pick(String nodeId, Collection<User> users) {
        return pickers.get(nodeId).pick(users);
    }

    private Picker<Room, User> of(PickingMode mode) {
        switch (mode) {
            case SEQUENTIAL_FILLING -> {
                return new SequentialFillingPicker(userRepository);
            }

            case ROUND_ROBIN -> {
                return new RoundRobinPicker(userRepository);
            }

            case LEAST_PICKED -> {
                return new LeastPickedPicker(userRepository);
            }

            default -> throw new InvalidParameterException("Taken: " + mode);
        }
    }
}
