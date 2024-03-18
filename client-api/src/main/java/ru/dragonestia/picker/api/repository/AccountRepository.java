package ru.dragonestia.picker.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.account.ResponseAccount;

import java.util.Optional;

public interface AccountRepository {

    Optional<ResponseAccount> findAccountByUsername(@NotNull String username);
}
