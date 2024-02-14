package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.repository.response.LinkedRoomsWithUserResponse;
import ru.dragonestia.picker.api.repository.response.SearchUserResponse;
import ru.dragonestia.picker.api.repository.response.UserDetailsResponse;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.DetailsParser;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.List;

@Tag(name = "Users", description = "User management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final DetailsParser detailsParser;
    private final NamingValidator namingValidator;

    @Operation(summary = "Search user by identifier")
    @GetMapping("/search")
    SearchUserResponse search(
            @Parameter(description = "User identifier input") @RequestParam(name = "input") String input,
            @Parameter(description = "Required addition user data", example = "COUNT_ROOMS") @RequestParam(name = "requiredDetails", required = false, defaultValue = "") String detailsSeq
    ) {
        if (!namingValidator.validateUserId(input) || input.isEmpty()) {
            return new SearchUserResponse(List.of());
        }

        return new SearchUserResponse(userService.searchUsers(input, detailsParser.parseUserDetails(detailsSeq)));
    }

    @Operation(summary = "Get user info")
    @GetMapping("/{userId}")
    UserDetailsResponse find(
            @Parameter(description = "User identifier") @PathVariable(value = "userId") String userId,
            @Parameter(description = "Required addition user data", example = "COUNT_ROOMS") @RequestParam(value = "requiredDetails", required = false) String detailsSeq
    ) {
        if (!namingValidator.validateUserId(userId)) {
            return new UserDetailsResponse(new RUser(userId));
        }

        return new UserDetailsResponse(userService.getUserDetails(userId, detailsParser.parseUserDetails(detailsSeq)));
    }

    @Operation(summary = "Get rooms linked with user")
    @GetMapping("/{userId}/rooms")
    LinkedRoomsWithUserResponse roomsOf(
            @Parameter(description = "User identifier") @PathVariable(value = "userId") String userId,
            @Parameter(description = "Required addition room data", example = "COUNT_USERS") @RequestParam(value = "requiredDetails", required = false) String detailsSeq
    ) {
        if (!namingValidator.validateUserId(userId)) {
            return new LinkedRoomsWithUserResponse(List.of());
        }

        return new LinkedRoomsWithUserResponse(userService.getUserRoomsWithDetails(new User(userId), detailsParser.parseRoomDetails(detailsSeq)));
    }
}
