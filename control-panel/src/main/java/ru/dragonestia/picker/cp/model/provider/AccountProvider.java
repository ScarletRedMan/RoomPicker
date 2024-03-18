package ru.dragonestia.picker.cp.model.provider;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.account.ResponseAccount;
import ru.dragonestia.picker.cp.model.Account;

public interface AccountProvider {

    @NotNull Account provide(@NotNull ResponseAccount responseAccount);
}
