package ru.dragonestia.picker.controller;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final DetailsParser detailsParser;
    private final NamingValidator namingValidator;

    @GetMapping("/search")
    SearchUserResponse search(@RequestParam(name = "input") String input,
                              @RequestParam(name = "requiredDetails", required = false, defaultValue = "") String detailsSeq) {

        if (!namingValidator.validateUserId(input) || input.isEmpty()) {
            return new SearchUserResponse(List.of());
        }

        return new SearchUserResponse(userService.searchUsers(input, detailsParser.parseUserDetails(detailsSeq)));
    }

    @GetMapping("/{userId}")
    UserDetailsResponse find(@PathVariable(value = "userId") String userId,
               @RequestParam(value = "requiredDetails", required = false) String detailsSeq) {

        if (!namingValidator.validateUserId(userId)) {
            return new UserDetailsResponse(new RUser(userId));
        }

        return new UserDetailsResponse(userService.getUserDetails(userId, detailsParser.parseUserDetails(detailsSeq)));
    }

    @GetMapping("/{userId}/rooms")
    LinkedRoomsWithUserResponse roomsOf(@PathVariable(value = "userId") String userId,
                                        @RequestParam(value = "requiredDetails", required = false) String detailsSeq) {

        if (!namingValidator.validateUserId(userId)) {
            return new LinkedRoomsWithUserResponse(List.of());
        }

        return new LinkedRoomsWithUserResponse(userService.getUserRoomsWithDetails(new User(userId), detailsParser.parseRoomDetails(detailsSeq)));
    }
}
