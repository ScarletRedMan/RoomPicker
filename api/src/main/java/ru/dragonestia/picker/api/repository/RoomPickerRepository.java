package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;

public interface RoomPickerRepository {

    RoomPickerInfoResponse getInfo();
}
