package ru.dragonestia.picker.api.impl;

import okhttp3.Credentials;
import okhttp3.Request;
import org.jetbrains.annotations.ApiStatus.Internal;

import ru.dragonestia.picker.api.impl.exception.ExceptionService;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.account.Account;
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
    private Account account;

    public RoomPickerClient(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.restTemplate = new RestTemplate(this, this::updateAccountData);
        this.instanceRepository = null; //new InstanceRepositoryImpl(this);
        this.roomRepository = null; //new RoomRepositoryImpl(this);
        this.entityRepository = null; //new EntityRepositoryImpl(this);
        this.accountRepository = null; //new AccountRepositoryImpl(this);

        ExceptionService.init();
    }

    @Internal
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Internal
    public Request.Builder prepareRequestBuilder(String uri) {
        return new Request.Builder()
                .url(url + uri)
                .addHeader("Authorization", Credentials.basic(username, password));
    }

    public InstanceRepository getNodeRepository() {
        return instanceRepository;
    }

    public RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public EntityRepository getUserRepository() {
        return entityRepository;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public RoomPickerInfoResponse getServerInfo() {
        return restTemplate.query("/info", HttpMethod.GET, RoomPickerInfoResponse.class, params -> {});
    }

    public Account getAccount() {
        if (account == null) {
            getServerInfo();
            assert account != null;
        }
        
        return account;
    }
    
    private void updateAccountData(Account account) {
        this.account = account;
    }
}
