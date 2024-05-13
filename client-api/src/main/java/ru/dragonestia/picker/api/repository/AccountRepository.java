package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.model.account.Account;
import ru.dragonestia.picker.api.model.account.AccountId;
import ru.dragonestia.picker.api.model.account.Permission;

import java.util.Collection;
import java.util.List;

public interface AccountRepository {

    List<AccountId> allAccountsIds();

    Account getAccount(AccountId id);

    List<Account> getAccounts(Collection<Account> ids);

    void createAccount(AccountId id, String password, List<Permission> permissions);

    void deleteAccount(AccountId id);

    void setPermissions(AccountId id, List<Permission> permissions);

    default void setPermissions(Account account, List<Permission> permissions) {
        setPermissions(account.id(), permissions);
    }

    void changePassword(AccountId id, String newPassword);

    default void changePassword(Account account, String newPassword) {
        changePassword(account.id(), newPassword);
    }
}
