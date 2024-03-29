package ru.dragonestia.picker.api.impl;

import okhttp3.Credentials;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

import ru.dragonestia.picker.api.impl.repository.AccountRepositoryImpl;
import ru.dragonestia.picker.api.impl.repository.NodeRepositoryImpl;
import ru.dragonestia.picker.api.impl.repository.RoomRepositoryImpl;
import ru.dragonestia.picker.api.impl.repository.UserRepositoryImpl;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.repository.AccountRepository;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;

public class RoomPickerClient {

    private final String url;
    private final String username;
    private final String password;
    private final RestTemplate restTemplate;
    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public RoomPickerClient(@NotNull String url, @NotNull String username, @NotNull String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.restTemplate = new RestTemplate(this);
        this.nodeRepository = new NodeRepositoryImpl(this);
        this.roomRepository = new RoomRepositoryImpl(this);
        this.userRepository = new UserRepositoryImpl(this);
        this.accountRepository = new AccountRepositoryImpl(this);
    }

    @Internal
    public @NotNull RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Internal
    public @NotNull Request.Builder prepareRequestBuilder(@NotNull String uri) {
        return new Request.Builder()
                .url(url + uri)
                .addHeader("Authorization", Credentials.basic(username, password));
    }

    public @NotNull NodeRepository getNodeRepository() {
        return nodeRepository;
    }

    public @NotNull RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public @NotNull UserRepository getUserRepository() {
        return userRepository;
    }

    public @NotNull  AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public @NotNull RoomPickerInfoResponse getServerInfo() {
        return restTemplate.query("/info", HttpMethod.GET, RoomPickerInfoResponse.class, params -> {});
    }
}
