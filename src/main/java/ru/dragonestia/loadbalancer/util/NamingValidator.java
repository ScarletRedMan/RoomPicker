package ru.dragonestia.loadbalancer.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NamingValidator {

    public boolean validateNodeIdentifier(String input) {
        return input.matches("^[a-z\\d-]+$");
    }

    public boolean validateBucketIdentifier(String input) {
        return input.matches("^[a-z\\d-]+$");
    }
}
