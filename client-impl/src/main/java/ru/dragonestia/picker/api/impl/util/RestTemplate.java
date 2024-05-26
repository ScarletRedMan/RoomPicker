package ru.dragonestia.picker.api.impl.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import okhttp3.*;
import org.jetbrains.annotations.ApiStatus.Internal;
import ru.dragonestia.picker.api.impl.exception.ExceptionService;
import ru.dragonestia.picker.api.impl.exception.GraphqlException;
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
import java.util.stream.Stream;

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
        execute(client.prepareRequestBuilder(uri + queryEncode(paramsConsumer))
                .method(method.name(), method == HttpMethod.GET? null : new FormBody.Builder().build())
                .build());
    }

    public <T> T queryWithRequest(String uri, HttpMethod method) {
        return queryWithRequest(uri, method, ParamsConsumer.NONE);
    }

    public <T> T queryWithRequest(String uri, HttpMethod method, ParamsConsumer paramsConsumer) {
        return queryWithRequest(uri, method, new TypeReference<>(){}, paramsConsumer);
    }

    public <T> T queryWithRequest(String uri, HttpMethod method, TypeReference<T> type, ParamsConsumer paramsConsumer) {
        return execute(client.prepareRequestBuilder(uri + queryEncode(paramsConsumer))
                .method(method.name(), method == HttpMethod.GET? null : new FormBody.Builder().build())
                .build(),
                type);
    }

    public <T> T queryPostWithBodyRequest(String uri, ParamsConsumer paramsConsumer, String body) {
        return queryPostWithBodyRequest(uri, new TypeReference<>() {}, paramsConsumer, body);
    }

    public <T> T queryPostWithBodyRequest(String uri, TypeReference<T> type, ParamsConsumer paramsConsumer, String body) {
        return execute(client.prepareRequestBuilder(uri + queryEncode(paramsConsumer))
                        .post(RequestBody.create(body, MediaType.get("text/plain")))
                        .build(), type);
    }

    public void queryPostWithBody(String uri, ParamsConsumer paramsConsumer, String body) {
        execute(client.prepareRequestBuilder(uri + queryEncode(paramsConsumer))
                .post(RequestBody.create(body, MediaType.get("text/plain")))
                .build());
    }

    public <T> T executeGraphQL(GraphqlQuery<T> query) {
        var map = new HashMap<String, String>();
        query.paramProvider().accept(map::put);

        String queryBody;
        try {
            queryBody = json.writeValueAsString(new GraphqlQuery.Request(query.query(), map));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        var request = client.prepareRequestBuilder("/graphql")
                .post(RequestBody.create(queryBody, MediaType.get("application/json")))
                .build();

        var response = executeGraphql(request);
        JsonNode node;
        try {
            node = json.readTree(response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (node.has("errors")) {
            var details = new HashMap<String, String>();
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                var entry = it.next();

                details.put(entry.getKey(), entry.getValue().textValue());
            }
            throw new GraphqlException(details);
        }

        try {
            return json.readValue(response, new TypeReference<GraphqlQuery.Response<T>>(){}).data();
        } catch (Exception ex) {
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

    private void execute(Request request) {
        try (var response = httpClient.newCall(request).execute()) {
            checkResponseForErrors(response);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing error", ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T> T execute(Request request, TypeReference<T> type) {
        try (var response = httpClient.newCall(request).execute()) {
            checkResponseForErrors(response);

            return json.readValue(new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8), type);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing error", ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String executeGraphql(Request request) {
        try (var response = httpClient.newCall(request).execute()) {
            checkResponseForErrors(response);

            return new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json processing error", ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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

            var exceptionClass = response.header("X-Server-Exception");
            var body = new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
            throw ExceptionService.prepare(exceptionClass, body);
        }

        if (statusCode == 5) {
            throw new RuntimeException("Internal server error");
        }

        if (statusCode == 2) {
            var accountData = json.readValue(response.header("X-Roompicker-Account"), Account.class);
            accountConsumer.accept(accountData);
        }
    }

    public interface ParamsConsumer extends Consumer<Map<String, String>> {

        ParamsConsumer NONE = map -> {};
    }
}
