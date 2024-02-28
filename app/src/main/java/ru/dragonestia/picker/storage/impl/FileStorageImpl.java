package ru.dragonestia.picker.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.storage.NodeAndRoomStorage;

import java.io.File;
import java.util.Objects;

@Log4j2
@Profile("!test")
@Component
@RequiredArgsConstructor
public class FileStorageImpl implements NodeAndRoomStorage {

    @Value("${ROOMPICKER_DATA_PATH:./appdata}")
    private String path;

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    @Override
    public void loadAll() {
        var dir = new File(path);
        var reader = objectMapper.reader();

        if (!dir.exists()) dir.mkdirs();

        var nodeDir = new File(path + "/nodes");
        if (!nodeDir.exists()) nodeDir.mkdir();
        for (var file: Objects.requireNonNull(nodeDir.listFiles(File::isFile))) {
            try {
                var node = reader.readValue(file, Node.class);
                nodeRepository.create(node);
            } catch (Exception ex) {
                log.error("Cannot read node file '%s'".formatted(file.getName()));
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
    public void saveNode(Node node) {
        if (!node.persist()) return;
        var nodeFile = new File(path + "/nodes/" + node.id() + ".json");
        var writer = objectMapper.writer();

        try {
            writer.writeValue(nodeFile, node);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (!node.persist()) return;
        new File(path + "/nodes/" + node.id() + ".json").delete();

        log.info("Removed node '%s' from disk storage".formatted(node.id()));
    }

    @SneakyThrows
    @Override
    public void saveRoom(Room room) {
        if (!room.isPersist()) return;
        var roomFile = new File(path + "/rooms/" + room.getNodeId() + "." + room.getId() + ".json");
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
        new File(path + "/rooms/" + room.getNodeId() + "." + room.getId() + ".json").delete();

        log.info("Removed room '%s/%s' from disk storage".formatted(room.getNodeId(), room.getId()));
    }
}
