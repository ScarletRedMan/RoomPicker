package ru.dragonestia.picker.model.account;

import lombok.Getter;
import ru.dragonestia.picker.exception.InvalidIdentifierException;

import java.util.Objects;

@Getter
public final class AccountId {

    private final String value;

    private AccountId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId = (AccountId) o;
        return Objects.equals(value, accountId.value);
    }

    public static AccountId of(String identifier) throws InvalidIdentifierException {
        if (identifier.matches("^[aA-zZ\\d]{3,32}$")) {
            return new AccountId(identifier);
        }

        throw InvalidIdentifierException.taken(identifier);
    }
}
