package ru.dragonestia.picker.api.model.account;

import java.util.List;

public record Account(AccountId id, List<Permission> permissions, boolean locked) {}
