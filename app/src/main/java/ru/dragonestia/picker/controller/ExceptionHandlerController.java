package ru.dragonestia.picker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dragonestia.picker.api.exception.*;
import ru.dragonestia.picker.controller.response.ErrorResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NodeNotFoundException.class)
    ResponseEntity<?> nodeNotFound(NodeNotFoundException ex) {
        return create(404, ex, details -> {
            details.put("node", ex.getNodeId());
        });
    }

    @ExceptionHandler(RoomNotFoundException.class)
    ResponseEntity<?> roomNotFound(RoomNotFoundException ex) {
        return create(404, ex, details -> {
            details.put("node", ex.getNodeId());
            details.put("room", ex.getRoomId());
        });
    }

    @ExceptionHandler(InvalidUsernamesException.class)
    ResponseEntity<?> invalidUsernames(InvalidUsernamesException ex) {
        return create(400, ex, details -> {
            Function<Collection<String>, String> toString = input -> String.join(",", input);

            details.put("givenUsernames", toString.apply(ex.getGivenUsernames()));
            details.put("invalidUsernames", toString.apply(ex.getInvalidUsernames()));
        });
    }

    @ExceptionHandler(InvalidNodeIdentifierException.class)
    ResponseEntity<?> invalidNodeIdentifier(InvalidNodeIdentifierException ex) {
        return create(400, ex, details -> {
            details.put("identifier", ex.getNodeId());
        });
    }

    @ExceptionHandler(InvalidRoomIdentifierException.class)
    ResponseEntity<?> invalidRoomIdentifier(InvalidRoomIdentifierException ex) {
        return create(400, ex, details -> {
            details.put("identifier", ex.getRoomId());
        });
    }

    @ExceptionHandler(NodeAlreadyExistException.class)
    ResponseEntity<?> nodeAlreadyExists(NodeAlreadyExistException ex) {
        return create(400, ex, details -> {
            details.put("nodeId", ex.getNodeId());
        });
    }

    @ExceptionHandler(RoomAlreadyExistException.class)
    ResponseEntity<?> roomAlreadyExists(RoomAlreadyExistException ex) {
        return create(400, ex, details -> {
            details.put("nodeId", ex.getNodeId());
            details.put("roomId", ex.getRoomId());
        });
    }

    @ExceptionHandler(RoomAreFullException.class)
    ResponseEntity<?> roomAreFull(RoomAreFullException ex) {
        return create(400, ex, details -> {
            details.put("nodeId", ex.getNodeId());
            details.put("roomId", ex.getRoomId());
        });
    }

    @ExceptionHandler(NoRoomsAvailableException.class)
    ResponseEntity<?> noRoomsAvailable(NoRoomsAvailableException ex) {
        return create(400, ex, details -> {
            details.put("nodeId", ex.getNodeId());
        });
    }

    private ResponseEntity<ErrorResponse> create(int code, ApiException ex, Consumer<Map<String, String>> detailsConsumer) {
        var details = new HashMap<String, String>();
        detailsConsumer.accept(details);

        return ResponseEntity.status(code).body(new ErrorResponse(ex.getErrorId(), ex.getMessage(), details));
    }
}
