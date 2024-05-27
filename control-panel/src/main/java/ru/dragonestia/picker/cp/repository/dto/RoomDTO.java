package ru.dragonestia.picker.cp.repository.dto;

public interface RoomDTO {

    String getId();

    String getInstanceId();

    int getSlots();

    boolean isLocked();

    int getCountEntities();

    boolean isPersist();
}
