package ru.dragonestia.picker.api.repository.response;

import ru.dragonestia.picker.api.model.account.ResponseAccount;

import java.util.List;

public record AllAccountsResponse(List<ResponseAccount> accounts) {}
