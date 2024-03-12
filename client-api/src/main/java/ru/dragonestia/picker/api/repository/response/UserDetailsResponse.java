package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.model.user.ResponseUser;

@Schema(title = "User details", hidden = true)
public record UserDetailsResponse(ResponseUser user) {}
