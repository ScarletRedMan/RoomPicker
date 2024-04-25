package ru.dragonestia.picker.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.account.IAccount;
import ru.dragonestia.picker.api.model.account.ResponseAccount;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Optional<ResponseAccount> findAccountByUsername(@NotNull String username);

    @NotNull List<ResponseAccount> allAccounts();

    void createAccount(@NotNull String accountId, @NotNull String password);

    void removeAccount(@NotNull IAccount account);

    void setPermissions(@NotNull IAccount account, @NotNull List<String> permissions);
}
