package ru.dragonestia.picker.api.impl.repository;

import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.model.account.Account;
import ru.dragonestia.picker.api.model.account.AccountId;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.api.repository.AccountRepository;

import java.util.Collection;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final RestTemplate restTemplate;

    public AccountRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<AccountId> allAccountsIds() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Account getAccount(AccountId id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Account> getAccounts(Collection<Account> ids) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void createAccount(AccountId id, String password, List<Permission> permissions) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAccount(AccountId id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setPermissions(AccountId id, List<Permission> permissions) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void changePassword(AccountId id, String newPassword) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
