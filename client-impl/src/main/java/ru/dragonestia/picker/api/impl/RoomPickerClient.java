package ru.dragonestia.picker.api.impl;

import okhttp3.Credentials;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

import ru.dragonestia.picker.api.impl.repository.AccountRepositoryImpl;
import ru.dragonestia.picker.api.impl.repository.InstanceRepositoryImpl;
import ru.dragonestia.picker.api.impl.repository.RoomRepositoryImpl;
import ru.dragonestia.picker.api.impl.repository.EntityRepositoryImpl;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.repository.AccountRepository;
import ru.dragonestia.picker.api.repository.InstanceRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.EntityRepository;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;

public class RoomPickerClient {

    private final String url;
    private final String username;
    private final String password;
    private final RestTemplate restTemplate;
    private final InstanceRepository instanceRepository;
    private final RoomRepository roomRepository;
    private final EntityRepository entityRepository;
    private final AccountRepository accountRepository;

    public RoomPickerClient(@NotNull String url, @NotNull String username, @NotNull String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.restTemplate = new RestTemplate(this);
        this.instanceRepository = new InstanceRepositoryImpl(this);
        this.roomRepository = new RoomRepositoryImpl(this);
        this.entityRepository = new EntityRepositoryImpl(this);
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

    public @NotNull InstanceRepository getNodeRepository() {
        return instanceRepository;
    }

    public @NotNull RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public @NotNull EntityRepository getUserRepository() {
        return entityRepository;
    }

    public @NotNull AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public @NotNull RoomPickerInfoResponse getServerInfo() {
        return restTemplate.query("/info", HttpMethod.GET, RoomPickerInfoResponse.class, params -> {});
    }
}
