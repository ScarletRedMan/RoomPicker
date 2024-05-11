package ru.dragonestia.picker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dragonestia.picker.exception.*;

@RestControllerAdvice
public class ExceptionHandlerController {

    private ResponseEntity<String> error(Exception ex, HttpStatus status) {
        return ResponseEntity.status(status)
                .header("X-Server-Exception", ex.getClass().getSimpleName())
                .body(ex.getMessage());
    }

    @ExceptionHandler(AdminAccountMutationException.class)
    ResponseEntity<String> adminAccountMutation(AdminAccountMutationException ex) {
        return error(ex, HttpStatus.LOCKED);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    ResponseEntity<String> alreadyExistsException(AlreadyExistsException ex) {
        return error(ex, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ConflictingPersistParametersException.class)
    ResponseEntity<String> conflictingPersistParameter(ConflictingPersistParametersException ex) {
        return error(ex, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DoesNotExistsException.class)
    ResponseEntity<String> doesNotExists(DoesNotExistsException ex) {
        return error(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidIdentifierException.class)
    ResponseEntity<String> invalidIdentifier(InvalidIdentifierException ex) {
        return error(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoRoomsAvailableException.class)
    ResponseEntity<String> noRoomsAvailable(NoRoomsAvailableException ex) {
        return error(ex, HttpStatus.INSUFFICIENT_STORAGE);
    }

    @ExceptionHandler(RoomAreFullException.class)
    ResponseEntity<String> roomAreFull(RoomAreFullException ex) {
        return error(ex, HttpStatus.INSUFFICIENT_STORAGE);
    }
}
