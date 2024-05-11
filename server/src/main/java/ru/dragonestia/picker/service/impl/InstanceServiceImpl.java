package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.exception.InvalidIdentifierException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.service.InstanceService;
import ru.dragonestia.picker.storage.InstanceAndRoomStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstanceServiceImpl implements InstanceService {

    private final InstanceRepository instanceRepository;
    private final RoomRepository roomRepository;
    private final InstanceAndRoomStorage storage;

    @Override
    public void create(Instance instance) throws InvalidIdentifierException, AlreadyExistsException {
        instanceRepository.create(instance);
        storage.saveInstance(instance);
    }

    @Override
    public void remove(InstanceId id) {
        for (var room: roomRepository.all(id)) {
            storage.removeRoom(room);
        }

        instanceRepository.delete(id);
        storage.removeInstance(id);
    }

    @Override
    public List<Instance> all() {
        return instanceRepository.all();
    }

    @Override
    public Optional<Instance> find(InstanceId id) {
        return instanceRepository.findById(id);
    }
}
