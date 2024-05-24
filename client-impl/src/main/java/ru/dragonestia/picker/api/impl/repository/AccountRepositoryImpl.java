package ru.dragonestia.picker.api.impl.repository;

import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.account.Account;
import ru.dragonestia.picker.api.model.account.AccountId;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.api.repository.AccountRepository;
import ru.dragonestia.picker.api.repository.response.ResponseObject;

import java.util.Collection;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final RestTemplate rest;

    public AccountRepositoryImpl(RestTemplate rest) {
        this.rest = rest;
    }

    @Override
    public List<AccountId> allAccountsIds() {
        List<String> id = rest.queryWithRequest("/accounts", HttpMethod.GET);
        return id.stream().map(AccountId::of).toList();
    }

    @Override
    public Account getAccount(AccountId id) {
        ResponseObject.RAccount account = rest.queryWithRequest("/accounts/target/" + id, HttpMethod.GET);
        return account.convert();
    }

    @Override
    public List<Account> getAccounts(Collection<AccountId> id) {
        List<ResponseObject.RAccount> accounts = rest.queryWithRequest("/accounts/list", HttpMethod.GET, params -> {
            params.put("id", String.join(",", id.stream().map(AccountId::getValue).toList()));
        });
        return accounts.stream().map(ResponseObject.RAccount::convert).toList();
    }

    @Override
    public void createAccount(AccountId id, String password, List<Permission> permissions) {
        rest.query("/accounts", HttpMethod.POST, params -> {
            params.put("username", id.getValue());
            params.put("password", password);
            params.put("permissions", String.join(",", permissions.stream().map(Permission::toString).toList()));
        });
    }

    @Override
    public void deleteAccount(AccountId id) {
        rest.query("/accounts/target/" + id.getValue(), HttpMethod.DELETE);
    }

    @Override
    public void setPermissions(AccountId id, List<Permission> permissions) {
        rest.query("/accounts/target/" + id.getValue() + "/permissions", HttpMethod.PUT, params -> {
            params.put("permissions", String.join(",", permissions.stream().map(Enum::name).toList()));
        });
    }

    @Override
    public void changePassword(AccountId id, String newPassword) {
        rest.query("/accounts/target/" + id.getValue() + "/password", HttpMethod.PUT, params -> {
            params.put("newPassword", newPassword);
        });
    }
}
