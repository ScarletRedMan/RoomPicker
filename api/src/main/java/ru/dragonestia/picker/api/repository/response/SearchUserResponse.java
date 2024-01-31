package ru.dragonestia.picker.api.repository.response;

import ru.dragonestia.picker.api.repository.response.type.RUser;

import java.util.List;

public record SearchUserResponse(List<RUser> users) {}
