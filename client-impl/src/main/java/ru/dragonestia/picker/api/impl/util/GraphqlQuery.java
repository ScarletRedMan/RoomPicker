package ru.dragonestia.picker.api.impl.util;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record GraphqlQuery<T>(String query, Class<T> responseClass, ParamProvider paramProvider) {

    public interface ParamConsumer extends BiConsumer<String, String> {

        default void put(String key, String value) {
            accept(key, value);
        }
    }

    public interface ParamProvider extends Consumer<ParamConsumer> {}

    public record Request(String query, Map<String, String> variables) {}

    public record Response<T>(T data) {}
}
