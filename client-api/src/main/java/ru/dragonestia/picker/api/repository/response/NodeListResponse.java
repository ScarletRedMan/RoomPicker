package ru.dragonestia.picker.api.repository.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.model.node.ResponseNode;

import java.util.List;

@Schema(title = "List of nodes", hidden = true)
public record NodeListResponse(List<ResponseNode> nodes) {}
