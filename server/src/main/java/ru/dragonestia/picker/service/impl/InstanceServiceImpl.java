package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.exception.InvalidInstanceIdentifierException;
import ru.dragonestia.picker.api.exception.InstanceAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;
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
    public void create(Instance instance) throws InvalidInstanceIdentifierException, InstanceAlreadyExistException {
        instanceRepository.create(instance);
        storage.saveInstance(instance);
    }

    @Override
    public void remove(Instance instance) {
        for (var room: roomRepository.all(instance)) {
            storage.removeRoom(room);
        }

        instanceRepository.delete(instance);
        storage.removeInstance(instance);
    }

    @Override
    public List<Instance> all() {
        return instanceRepository.all();
    }

    @Override
    public Optional<Instance> find(String nodeId) {
        return instanceRepository.findById(nodeId);
    }
}
