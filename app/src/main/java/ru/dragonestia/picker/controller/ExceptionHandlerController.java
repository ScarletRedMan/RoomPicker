package ru.dragonestia.picker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dragonestia.picker.controller.response.ErrorResponse;
import ru.dragonestia.picker.exception.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NodeNotFoundException.class)
    ResponseEntity<?> nodeNotFound(NodeNotFoundException ex) {
        return create(404, "err.node.not_found", ex.getMessage(), details -> {
            details.put("node", ex.getNodeId());
        });
    }

    @ExceptionHandler(RoomNotFoundException.class)
    ResponseEntity<?> roomNotFound(RoomNotFoundException ex) {
        return create(404, "err.room.not_found", ex.getMessage(), details -> {
            details.put("node", ex.getNodeId());
            details.put("room", ex.getRoomId());
        });
    }

    @ExceptionHandler(InvalidUsernamesException.class)
    ResponseEntity<?> invalidUsernames(InvalidUsernamesException ex) {
        return create(400, "err.user.invalid_identifier", ex.getMessage(), details -> {
            Function<Collection<String>, String> toString = input -> String.join(",", input);

            details.put("givenUsernames", toString.apply(ex.getGivenUsernames()));
            details.put("invalidUsernames", toString.apply(ex.getUsernamesWithErrors()));
        });
    }

    @ExceptionHandler(InvalidNodeIdentifierException.class)
    ResponseEntity<?> invalidNodeIdentifier(InvalidNodeIdentifierException ex) {
        return create(400, "err.node.invalid_identifier", ex.getMessage(), details -> {
            details.put("identifier", ex.getNodeId());
        });
    }

    @ExceptionHandler(InvalidRoomIdentifierException.class)
    ResponseEntity<?> invalidRoomIdentifier(InvalidRoomIdentifierException ex) {
        return create(400, "err.room.invalid_identifier", ex.getMessage(), details -> {
            details.put("identifier", ex.getRoomId());
        });
    }

    @ExceptionHandler(NodeAlreadyExistException.class)
    ResponseEntity<?> nodeAlreadyExists(NodeAlreadyExistException ex) {
        return create(400, "err.node.already_exists", ex.getMessage(), details -> {
            details.put("nodeId", ex.getNodeId());
        });
    }

    @ExceptionHandler(RoomAlreadyExistException.class)
    ResponseEntity<?> roomAlreadyExists(RoomAlreadyExistException ex) {
        return create(400, "err.room.already_exists", ex.getMessage(), details -> {
            details.put("nodeId", ex.getNodeId());
            details.put("roomId", ex.getRoomId());
        });
    }

    @ExceptionHandler(RoomAreFullException.class)
    ResponseEntity<?> roomAreFull(RoomAreFullException ex) {
        return create(400, "err.room.are_full", ex.getMessage(), details -> {
            details.put("nodeId", ex.getNodeId());
            details.put("roomId", ex.getRoomId());
        });
    }

    @ExceptionHandler(NoRoomsAvailableException.class)
    ResponseEntity<?> noRoomsAvailable(NoRoomsAvailableException ex) {
        return create(400, "err.room.no_available", ex.getMessage(), details -> {
            details.put("nodeId", ex.getNodeId());
        });
    }

    private ResponseEntity<ErrorResponse> create(int code, String errorId, String message, Consumer<Map<String, String>> detailsConsumer) {
        var details = new HashMap<String, String>();
        detailsConsumer.accept(details);

        return ResponseEntity.status(code).body(new ErrorResponse(errorId, message, details));
    }
}
