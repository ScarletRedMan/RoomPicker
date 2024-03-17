package ru.dragonestia.picker.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.dragonestia.picker.model.Account;

import java.util.Collection;

public interface AccountService extends UserDetailsService {

    @NotNull Account createNewAccount(@NotNull String username, @NotNull String password);

    @NotNull Collection<Account> allAccounts();

    void removeAccount(@NotNull Account account);
}
