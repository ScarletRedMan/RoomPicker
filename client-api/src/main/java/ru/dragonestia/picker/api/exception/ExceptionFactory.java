package ru.dragonestia.picker.api.exception;

import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ExceptionFactory {

    private final static Map<String, Constructor> factory = init();

    private ExceptionFactory() {}

    private static Map<String, Constructor> init() {
        var factory = new HashMap<String, Constructor>();

        factory.put(InvalidNodeIdentifierException.ERROR_ID, InvalidNodeIdentifierException::new);
        factory.put(InvalidRoomIdentifierException.ERROR_ID, InvalidRoomIdentifierException::new);
        factory.put(InvalidUsernamesException.ERROR_ID, InvalidUsernamesException::new);
        factory.put(NodeAlreadyExistException.ERROR_ID, NodeAlreadyExistException::new);
        factory.put(NodeNotFoundException.ERROR_ID, NodeNotFoundException::new);
        factory.put(NoRoomsAvailableException.ERROR_ID, NoRoomsAvailableException::new);
        factory.put(RoomAlreadyExistException.ERROR_ID, RoomAlreadyExistException::new);
        factory.put(RoomAreFullException.ERROR_ID, RoomAreFullException::new);
        factory.put(RoomNotFoundException.ERROR_ID, RoomNotFoundException::new);
        factory.put(NotPersistedNodeException.ERROR_ID, NotPersistedNodeException::new);
        factory.put(AccountDoesNotExistsException.ERROR_ID, AccountDoesNotExistsException::new);
        factory.put(PermissionNotFoundException.ERROR_ID, PermissionNotFoundException::new);
        factory.put(ConstantAdminParamsException.ERROR_ID, err -> new ConstantAdminParamsException());

        return factory;
    }

    public static ApiException of(ErrorResponse errorResponse) {
        return factory.get(errorResponse.errorId()).apply(errorResponse);
    }

    private interface Constructor extends Function<ErrorResponse, ApiException> {}
}
