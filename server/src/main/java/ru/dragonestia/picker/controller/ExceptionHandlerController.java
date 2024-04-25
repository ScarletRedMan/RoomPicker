package ru.dragonestia.picker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dragonestia.picker.api.exception.*;
import ru.dragonestia.picker.api.repository.response.ErrorResponse;

import java.util.HashMap;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NodeNotFoundException.class)
    ResponseEntity<?> nodeNotFound(NodeNotFoundException ex) {
        return create(404, ex);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    ResponseEntity<?> roomNotFound(RoomNotFoundException ex) {
        return create(404, ex);
    }

    @ExceptionHandler(InvalidUsernamesException.class)
    ResponseEntity<?> invalidUsernames(InvalidUsernamesException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(InvalidNodeIdentifierException.class)
    ResponseEntity<?> invalidNodeIdentifier(InvalidNodeIdentifierException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(InvalidRoomIdentifierException.class)
    ResponseEntity<?> invalidRoomIdentifier(InvalidRoomIdentifierException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(NodeAlreadyExistException.class)
    ResponseEntity<?> nodeAlreadyExists(NodeAlreadyExistException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(RoomAlreadyExistException.class)
    ResponseEntity<?> roomAlreadyExists(RoomAlreadyExistException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(RoomAreFullException.class)
    ResponseEntity<?> roomAreFull(RoomAreFullException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(NoRoomsAvailableException.class)
    ResponseEntity<?> noRoomsAvailable(NoRoomsAvailableException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(NotPersistedNodeException.class)
    ResponseEntity<?> notPersistedNode(NotPersistedNodeException ex) {
        return create(400, ex);
    }

    @ExceptionHandler(AccountDoesNotExistsException.class)
    ResponseEntity<?> accountDoesNotExists(AccountDoesNotExistsException ex) {
        return create(404, ex);
    }

    @ExceptionHandler({PermissionNotFoundException.class})
    ResponseEntity<?> permissionNotFound(PermissionNotFoundException ex) {
        return create(400, ex);
    }

    private ResponseEntity<ErrorResponse> create(int code, ApiException ex) {
        var details = new HashMap<String, String>();
        ex.appendDetailsToErrorResponse(details);

        return ResponseEntity.status(code).body(new ErrorResponse(ex.getErrorId(), ex.getMessage(), details));
    }
}
