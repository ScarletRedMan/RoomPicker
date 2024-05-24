package ru.dragonestia.picker.api.impl.exception;

import java.util.Collections;
import java.util.Map;

public class GraphqlException extends RuntimeException {

    private final Map<String, String> details;

    public GraphqlException(Map<String, String> details) {
        super(details.get("message"));
        this.details = details;
    }

    public Map<String, String> getDetails() {
        return Collections.unmodifiableMap(details);
    }
}
