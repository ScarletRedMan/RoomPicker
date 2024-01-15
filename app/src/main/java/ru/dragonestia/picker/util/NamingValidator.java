package ru.dragonestia.picker.util;

import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.InvalidUsernamesException;
import ru.dragonestia.picker.model.User;

import java.util.LinkedList;
import java.util.List;

@Component
public class NamingValidator {

    public void validateNodeId(String input) throws InvalidNodeIdentifierException {
        if (input.matches("^[a-z\\d-]+$")) return;

        throw new InvalidNodeIdentifierException(input);
    }

    public void validateRoomId(String input) throws InvalidRoomIdentifierException {
        if (input.matches("^[a-z\\d-]+$")) return;

        throw new InvalidRoomIdentifierException(input);
    }

    private boolean validateUserId(String input) {
        return input.matches("^[aA-zZ\\d-.\\s:/@%?!~$)(+=_|;*]+$");
    }

    public List<User> validateUserIds(List<String> input) throws InvalidUsernamesException {
        var users = new LinkedList<User>();
        var invalid = new LinkedList<String>();

        for (var username: input) {
            if (validateUserId(username)) {
                users.add(new User(username));
                continue;
            }

            invalid.add(username);
        }

        if (!invalid.isEmpty()) {
            throw new InvalidUsernamesException(input, invalid);
        }

        return users;
    }
}
