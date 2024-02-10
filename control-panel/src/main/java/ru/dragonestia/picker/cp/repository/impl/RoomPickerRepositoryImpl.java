package ru.dragonestia.picker.cp.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import ru.dragonestia.picker.api.repository.RoomPickerRepository;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;

@RequiredArgsConstructor
@SpringComponent
public class RoomPickerRepositoryImpl implements RoomPickerRepository {

    private final RestUtil rest;

    @Override
    public RoomPickerInfoResponse getInfo() {
        return rest.query("/info", HttpMethod.GET, RoomPickerInfoResponse.class, params -> {});
    }
}
