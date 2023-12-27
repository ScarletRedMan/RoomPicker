package ru.dragonestia.picker.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NamingValidator {

    public boolean validateNodeId(String input) {
        return input.matches("^[a-z\\d-]+$");
    }

    public boolean validateRoomId(String input) {
        return input.matches("^[a-z\\d-]+$");
    }

    public boolean validateUserId(String input) {
        return input.matches("^[aA-zZ\\d-.\\s:/@%?!~$)(+=_|;*]+$");
    }
}
