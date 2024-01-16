package ru.dragonestia.picker.cp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.dragonestia.picker.api.exception.ExceptionFactory;
import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class RestUtil {

    private final URI serverUrl;
    private final Supplier<RestTemplate> restTemplateSupplier;

    public void query(String uri, HttpMethod method) {
        query(uri, method, ParamsConsumer.NONE);
    }

    public void query(String uri, HttpMethod method, ParamsConsumer paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        var template = restTemplateSupplier.get();
        try {
            template.exchange(buildPath(uri, params.keySet()), method, null, String.class, params);
        } catch (HttpClientErrorException ex) {
            throw ExceptionFactory.of(Objects.requireNonNull(ex.getResponseBodyAs(ErrorResponse.class)));
        }
    }

    public <T> T query(String uri, HttpMethod method, Class<T> clazz) {
        return query(uri, method, clazz, ParamsConsumer.NONE);
    }

    public <T> T query(String uri, HttpMethod method, Class<T> clazz, ParamsConsumer paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        var template = restTemplateSupplier.get();
        try {
            return template.exchange(buildPath(uri, params.keySet()), method, null, clazz, params).getBody();
        } catch (HttpClientErrorException ex) {
            throw ExceptionFactory.of(Objects.requireNonNull(ex.getResponseBodyAs(ErrorResponse.class)));
        }
    }

    private String buildPath(String uri, Collection<String> paramKeys) {
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

    public interface ParamsConsumer extends Consumer<Map<String, String>> {

        ParamsConsumer NONE = map -> {};
    }
}
