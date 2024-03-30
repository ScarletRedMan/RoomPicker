package ru.dragonestia.picker.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.model.Account;
import ru.dragonestia.picker.model.Permission;
import ru.dragonestia.picker.service.AccountService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        var account = createNewAccount("admin", "qwerty123");
        account.setAuthorities(Arrays.stream(Permission.values()).collect(Collectors.toSet()));

        createNewAccount("test", "qwerty123");
    }

    public @NotNull Account createNewAccount(@NotNull String username, @NotNull String password) {
        var account = new Account(username, passwordEncoder.encode(password));
        accounts.put(account.getUsername().toLowerCase(), account);
        return account;
    }

    @Override
    public @NotNull Collection<Account> allAccounts() {
        return accounts.values();
    }

    @Override
    public void removeAccount(@NotNull Account account) {
        accounts.remove(account.getUsername());
        account.setEnabled(false);
    }

    @Override
    public void updateState(@NotNull Account account) {
        // TODO: save data to local storage
    }

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        var lowerUsername = username.toLowerCase();
        if (accounts.containsKey(lowerUsername)) {
            return accounts.get(lowerUsername);
        }

        throw new UsernameNotFoundException("User '" + username + "' does not exists");
    }
}
