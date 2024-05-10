package ru.dragonestia.picker.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.dragonestia.picker.model.account.Account;
import ru.dragonestia.picker.model.account.AccountId;

import java.util.Collection;
import java.util.Optional;

public interface AccountService extends UserDetailsService {

    @PreAuthorize("hasRole('ADMIN')")
    Account createNewAccount(AccountId id, String password);

    Optional<Account> findAccount(String accountId);

    @PreAuthorize("hasRole('ADMIN')")
    Collection<Account> allAccounts();

    @PreAuthorize("hasRole('ADMIN')")
    void removeAccount(Account account);

    @PreAuthorize("hasRole('ADMIN') || principal.username.equals(account.username)")
    void updateState(Account account);

    @Override
    Account loadUserByUsername(String username) throws UsernameNotFoundException;
}
