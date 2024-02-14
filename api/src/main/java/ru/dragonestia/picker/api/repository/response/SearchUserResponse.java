package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.repository.response.type.RUser;

import java.util.List;

@Schema(title = "Search user", hidden = true)
public record SearchUserResponse(List<RUser> users) {}
