package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.exception.AccountDoesNotExistsException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.account.IAccount;
import ru.dragonestia.picker.api.model.account.ResponseAccount;
import ru.dragonestia.picker.api.repository.AccountRepository;
import ru.dragonestia.picker.api.repository.response.AllAccountsResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AccountRepositoryImpl implements AccountRepository {

    private final RestTemplate rest;

    @Internal
    public AccountRepositoryImpl(RoomPickerClient client) {
        this.rest = client.getRestTemplate();
    }

    @Override
    public Optional<ResponseAccount> findAccountByUsername(@NotNull String username) {
        try {
            var response = rest.query("/accounts/" + username, HttpMethod.GET, ResponseAccount.class);
            return Optional.of(response);
        } catch (AccountDoesNotExistsException ex) {
            return Optional.empty();
        }
    }

    @Override
    public @NotNull List<ResponseAccount> allAccounts() {
        var response = rest.query("/accounts", HttpMethod.GET, AllAccountsResponse.class);
        return response.accounts();
    }

    @Override
    public void createAccount(@NotNull String accountId, @NotNull String password, @NotNull Set<String> permissions) {
        rest.query("/accounts", HttpMethod.POST, params -> {
            params.put("username", accountId);
            params.put("password", password);
            params.put("permissions", String.join(",", permissions));
        });
    }

    @Override
    public void removeAccount(@NotNull IAccount account) {
        rest.query("/accounts/" + account.getUsername(), HttpMethod.DELETE);
    }

    @Override
    public void setPermissions(@NotNull IAccount account, @NotNull List<String> permissions) {
        rest.query("/accounts/" + account.getUsername(), HttpMethod.PUT, params -> {
            params.put("permissions", String.join(",", permissions));
        });
    }

    @Override
    public void setPassword(@NotNull IAccount account, @NotNull String newPassword) {
        rest.query("/accounts/" + account.getUsername() + "/password", HttpMethod.PUT, params -> {
            params.put("newPassword", newPassword);
        });
    }
}
