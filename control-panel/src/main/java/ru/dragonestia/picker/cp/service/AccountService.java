package ru.dragonestia.picker.cp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.cp.model.Account;
import ru.dragonestia.picker.cp.model.provider.AccountProvider;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final RoomPickerClient adminClient;
    private final AccountProvider accountProvider;

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        var response = adminClient.getAccountRepository().findAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return accountProvider.provide(response);
    }
}
