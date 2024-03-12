package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.model.user.ResponseUser;

import java.util.List;

@Schema(title = "Search user", hidden = true)
public record SearchUserResponse(List<ResponseUser> users) {}
