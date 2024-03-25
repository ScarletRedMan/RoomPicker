package ru.dragonestia.picker.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.dragonestia.picker.model.Account;

import java.util.Collection;

public interface AccountService extends UserDetailsService {

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull Account createNewAccount(@NotNull String username, @NotNull String password);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull Collection<Account> allAccounts();

    @PreAuthorize("hasRole('ADMIN')")
    void removeAccount(@NotNull Account account);

    @Override
    Account loadUserByUsername(String username) throws UsernameNotFoundException;
}
