package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.repository.response.type.RNode;

@Schema(title = "Node details", hidden = true)
public record NodeDetailsResponse(RNode node) {}
