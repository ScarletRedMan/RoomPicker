package ru.dragonestia.picker.repository.impl.cache;

import org.springframework.stereotype.Component;
import ru.dragonestia.picker.repository.impl.picker.RoomPicker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NodeId2PickerModeCache {

    private final Map<String, RoomPicker> cache = new ConcurrentHashMap<>();

    public void put(String nodeId, RoomPicker picker) {
        cache.put(nodeId, picker);
    }

    public void remove(String nodeId) {
        cache.remove(nodeId);
    }

    public RoomPicker get(String nodeId) {
        return cache.get(nodeId);
    }
}
