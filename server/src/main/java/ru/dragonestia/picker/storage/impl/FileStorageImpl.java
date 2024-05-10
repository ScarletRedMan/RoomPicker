package ru.dragonestia.picker.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.storage.InstanceAndRoomStorage;

import java.io.File;
import java.util.Objects;

@Log4j2
@Profile("!test")
@Component
@RequiredArgsConstructor
public class FileStorageImpl implements InstanceAndRoomStorage {

    @Value("${ROOMPICKER_DATA_PATH:./appdata}")
    private String path;

    private final InstanceRepository instanceRepository;
    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    @Override
    public void loadAll() {
        var dir = new File(path);
        var reader = objectMapper.reader();

        if (!dir.exists()) dir.mkdirs();

        var nodeDir = new File(path + "/instances");
        if (!nodeDir.exists()) nodeDir.mkdir();
        for (var file: Objects.requireNonNull(nodeDir.listFiles(File::isFile))) {
            try {
                var instance = reader.readValue(file, Instance.class);
                instanceRepository.create(instance);
            } catch (Exception ex) {
                log.error("Cannot read instance file '%s'".formatted(file.getName()));
                log.throwing(ex);
            }
        }

        var roomDir = new File(path + "/rooms");
        if (!roomDir.exists()) roomDir.mkdir();
        for (var file: Objects.requireNonNull(roomDir.listFiles(File::isFile))) {
            try {
                var room = reader.readValue(file, Room.class);
                roomRepository.create(room);
            } catch (Exception ex) {
                log.error("Cannot read room file '%s'".formatted(file.getName()));
                log.throwing(ex);
            }
        }
    }

    @Override
    public void saveInstance(Instance instance) {
        if (!instance.isPersist()) return;
        var instanceFile = new File(path + "/instances/" + instance.getIdentifier() + ".json");
        var writer = objectMapper.writer();

        try {
            writer.writeValue(instanceFile, instance);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeInstance(Instance instance) {
        if (!instance.isPersist()) return;
        new File(path + "/nodes/" + instance.getIdentifier() + ".json").delete();

        log.info("Removed instance '%s' from disk storage".formatted(instance.getIdentifier()));
    }

    @SneakyThrows
    @Override
    public void saveRoom(Room room) {
        if (!room.isPersist()) return;
        var roomFile = new File("%s/rooms/%s.%s.json".formatted(path, room.getInstanceIdentifier(), room.getIdentifier()));
        var writer = objectMapper.writer();

        try {
            writer.writeValue(roomFile, room);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeRoom(Room room) {
        if (!room.isPersist()) return;
        new File("%s/rooms/%s.%s.json".formatted(path, room.getInstanceIdentifier(), room.getIdentifier())).delete();

        log.info("Removed room '%s/%s' from disk storage".formatted(room.getInstanceIdentifier(), room.getIdentifier()));
    }
}
