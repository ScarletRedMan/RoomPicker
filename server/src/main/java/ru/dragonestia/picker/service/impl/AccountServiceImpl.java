package ru.dragonestia.picker.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.config.RoomPickerServerConfig;
import ru.dragonestia.picker.exception.AdminAccountMutationException;
import ru.dragonestia.picker.model.account.Account;
import ru.dragonestia.picker.model.account.AccountId;
import ru.dragonestia.picker.model.account.Permission;
import ru.dragonestia.picker.service.AccountService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final RoomPickerServerConfig.AdminCredentials adminCredentials;

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        var account = createNewAccount(AccountId.of(adminCredentials.username()), adminCredentials.password());
        account.setAuthorities(Arrays.stream(Permission.values()).collect(Collectors.toSet()));

        createNewAccount(AccountId.of("test"), "qwerty123");
    }

    public Account createNewAccount(AccountId id, String password) {
        var account = new Account(id, passwordEncoder.encode(password));
        accounts.put(account.getUsername().toLowerCase(), account);
        return account;
    }

    @Override
    public Optional<Account> findAccount(String accountId) {
        return Optional.ofNullable(accounts.getOrDefault(accountId, null));
    }

    @Override
    public Collection<Account> allAccounts() {
        return accounts.values().stream()
                .filter(account -> !adminCredentials.username().equals(account.getUsername()))
                .toList();
    }

    @Override
    public void removeAccount(Account account) {
        checkAdmin(account.getUsername());
        accounts.remove(account.getUsername());
        account.setEnabled(false);
    }

    @Override
    public void updateState(Account account) {
        checkAdmin(account.getUsername());
        // TODO: save data to local storage
    }

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        var lowerUsername = username.toLowerCase();
        if (accounts.containsKey(lowerUsername)) {
            return accounts.get(lowerUsername);
        }

        throw new UsernameNotFoundException("Entity '" + username + "' does not exists");
    }

    private void checkAdmin(String accountId) {
        if (adminCredentials.username().equals(accountId)) {
            throw new AdminAccountMutationException();
        }
    }
}
