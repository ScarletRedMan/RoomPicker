package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.repository.response.type.RUser;

@Schema(title = "User details", hidden = true)
public record UserDetailsResponse(RUser user) {}
