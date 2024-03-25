package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.exception.AccountDoesNotExistsException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.account.ResponseAccount;
import ru.dragonestia.picker.api.repository.AccountRepository;

import java.util.Optional;

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
}
