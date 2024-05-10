package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.repository.response.LinkedRoomsWithUserResponse;
import ru.dragonestia.picker.api.repository.response.SearchUserResponse;
import ru.dragonestia.picker.api.repository.response.UserDetailsResponse;

@Tag(name = "Users", description = "User management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    @Operation(summary = "Search user by identifier")
    @GetMapping("/search")
    SearchUserResponse search(
            @Parameter(description = "User identifier input") @RequestParam(name = "input") String input
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Get user info")
    @GetMapping("/{userId}")
    UserDetailsResponse find(
            @Parameter(description = "User identifier") @PathVariable(value = "userId") String userId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Get rooms linked with user")
    @GetMapping("/{userId}/rooms")
    LinkedRoomsWithUserResponse roomsOf(
            @Parameter(description = "User identifier") @PathVariable(value = "userId") String userId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
