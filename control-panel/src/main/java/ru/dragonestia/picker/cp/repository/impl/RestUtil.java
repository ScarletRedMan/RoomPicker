package ru.dragonestia.picker.cp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class RestUtil {

    private final URI serverUrl;
    private final Supplier<RestTemplate> restTemplate;

    public <T> T get(URI uri, Class<T> responseType) {
        var template = restTemplate.get();
        return Objects.requireNonNull(template.getForObject(serverUrl.resolve(uri), responseType));
    }

    public <T> ResponseEntity<T> getEntity(URI uri, Class<T> responseType) {
        var template = restTemplate.get();
        return template.getForEntity(serverUrl.resolve(uri), responseType);
    }

    public <T> T get(URI uri, Class<T> responseType, Consumer<Map<String, String>> paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        var template = restTemplate.get();
        return Objects.requireNonNull(template.getForObject(buildPath(uri, params.keySet()),
                responseType,
                params));
    }

    public <T> T post(URI uri, Class<T> responseType, Consumer<Map<String, String>> paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        var template = restTemplate.get();
        return Objects.requireNonNull(template.postForObject(buildPath(uri, params.keySet()),
                null,
                responseType,
                params));
    }

    public <T> ResponseEntity<T> postEntity(URI uri, Class<T> responseType, Consumer<Map<String, String>> paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        var template = restTemplate.get();
        return template.postForEntity(buildPath(uri, params.keySet()),
                null,
                responseType,
                params);
    }

    public void put(URI uri, Consumer<Map<String, String>> paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        var template = restTemplate.get();
        template.put(buildPath(uri, params.keySet()), params);
    }

    public void delete(URI uri, Consumer<Map<String, String>> paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        var template = restTemplate.get();
        template.delete(buildPath(uri, params.keySet()), params);
    }

    private String buildPath(URI uri, Collection<String> paramKeys) {
        var path = new StringBuilder(serverUrl.resolve(uri) + "?");
        int left = paramKeys.size();
        for (var key: paramKeys) {
            path.append(key);
            path.append("={");
            path.append(key);
            path.append("}");
            if (--left > 0) {
                path.append("&");
            }
        }
        return path.toString();
    }
}
