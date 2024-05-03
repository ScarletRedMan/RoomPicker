package ru.dragonestia.picker.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.dragonestia.picker.model.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountService extends UserDetailsService {

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull Account createNewAccount(@NotNull String username, @NotNull String password);

    @NotNull Optional<Account> findAccount(@NotNull String accountId);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull Collection<Account> allAccounts();

    @PreAuthorize("hasRole('ADMIN')")
    void removeAccount(@NotNull Account account);

    @PreAuthorize("hasRole('ADMIN') || principal.username.equals(account.username)")
    void updateState(@NotNull Account account);

    @Override
    Account loadUserByUsername(String username) throws UsernameNotFoundException;
}
