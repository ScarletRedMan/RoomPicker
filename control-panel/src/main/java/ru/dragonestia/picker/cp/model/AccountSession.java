package ru.dragonestia.picker.cp.model;

import lombok.Getter;
import lombok.Setter;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.account.Account;

@Setter
@Getter
public class AccountSession {

    private final Account data;
    private String password;
    private final RoomPickerClient client;

    public AccountSession(Account data, String password, RoomPickerClient client) {
        this.data = data;
        this.password = password;
        this.client = client;
    }
}
