package ru.dragonestia.picker.api.impl.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.jetbrains.annotations.ApiStatus.Internal;
import ru.dragonestia.picker.api.impl.exception.ExceptionService;
import ru.dragonestia.picker.api.impl.exception.NotEnoughPermissions;
import ru.dragonestia.picker.api.impl.exception.AuthException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.account.Account;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

@Internal
public class RestTemplate {

    private final RoomPickerClient client;
    private final Consumer<Account> accountConsumer;
    private final OkHttpClient httpClient;
    private final ObjectMapper json;

    public RestTemplate(RoomPickerClient client, Consumer<Account> accountConsumer) {
        this.client = client;
        this.accountConsumer = accountConsumer;

        httpClient = new OkHttpClient();
        json = configureJackson();
    }

    private ObjectMapper configureJackson() {
        var mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper;
    }

    public void query(String uri, HttpMethod method) {
        query(uri, method, ParamsConsumer.NONE);
    }

    public void query(String uri, HttpMethod method, ParamsConsumer paramsConsumer) {
        var request = client.prepareRequestBuilder(uri + queryEncode(paramsConsumer))
                .method(method.name(), method == HttpMethod.GET? null : new FormBody.Builder().build())
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            checkResponseForErrors(response);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing error", ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T query(String uri, HttpMethod method, Class<T> clazz) {
        return query(uri, method, clazz, ParamsConsumer.NONE);
    }

    public <T> T query(String uri, HttpMethod method, Class<T> clazz, ParamsConsumer paramsConsumer) {
        var request = client.prepareRequestBuilder(uri + queryEncode(paramsConsumer))
                .method(method.name(), method == HttpMethod.GET? null : new FormBody.Builder().build())
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            checkResponseForErrors(response);

            return json.readValue(new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8), clazz);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing error", ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T queryPostWithBody(String uri, Class<T> clazz, ParamsConsumer paramsConsumer, String body) {
        var request = client.prepareRequestBuilder(uri + queryEncode(paramsConsumer))
                .post(RequestBody.create(body, MediaType.get("text/plain")))
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            checkResponseForErrors(response);

            return json.readValue(new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8), clazz);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing error", ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String queryEncode(ParamsConsumer paramsConsumer) {
        var params = new HashMap<String, String>();
        paramsConsumer.accept(params);

        if (params.isEmpty()) return "";

        List<String> pairs = new ArrayList<>();
        for (var entry: params.entrySet()) {
            pairs.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return "?" + String.join("&", pairs);
    }

    private void checkResponseForErrors(Response response) throws IOException {
        var code = response.code();
        var statusCode = code / 100;

        if (statusCode == 4) {
            if (code == 401) {
                throw new AuthException("Invalid username and password");
            }
            if (code == 403) {
                throw new NotEnoughPermissions("Not enough permissions");
            }

            var accountData = json.readValue(response.header("X-Account"), Account.class);
            accountConsumer.accept(accountData);

            var exceptionClass = response.header("X-Server-Exception");
            var body = new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
            throw ExceptionService.prepare(exceptionClass, body);
        }

        if (statusCode == 5) {
            throw new RuntimeException("Internal server error");
        }
    }

    public interface ParamsConsumer extends Consumer<Map<String, String>> {

        ParamsConsumer NONE = map -> {};
    }
}
