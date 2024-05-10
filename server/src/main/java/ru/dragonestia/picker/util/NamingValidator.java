package ru.dragonestia.picker.util;

import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.InvalidInstanceIdentifierException;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.InvalidUsernamesException;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.api.util.IdentifierValidator;
import ru.dragonestia.picker.model.user.User;

import java.util.Collection;
import java.util.LinkedList;

@Component
public class NamingValidator {

    public void validateInstanceId(String input) throws InvalidInstanceIdentifierException {
        if (IdentifierValidator.forNode(input)) return;

        throw new InvalidInstanceIdentifierException(input);
    }

    public void validateRoomId(String nodeId, String input) throws InvalidRoomIdentifierException {
        if (IdentifierValidator.forRoom(input)) return;

        throw new InvalidRoomIdentifierException(nodeId, input);
    }

    public boolean validateUserId(String input) {
        return IdentifierValidator.forUser(input);
    }

    public void validateUserIds(Collection<String> input) throws InvalidUsernamesException {
        var users = new LinkedList<User>();
        var invalid = new LinkedList<String>();

        for (var username: input) {
            if (validateUserId(username)) {
                users.add(new User(UserIdentifier.of(username)));
                continue;
            }

            invalid.add(username);
        }

        if (!invalid.isEmpty()) {
            throw new InvalidUsernamesException(input.stream().toList(), invalid);
        }
    }
}
