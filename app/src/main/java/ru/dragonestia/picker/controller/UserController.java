package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dragonestia.picker.api.repository.details.UserDetails;
import ru.dragonestia.picker.api.repository.response.SearchUserResponse;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final NamingValidator namingValidator;

    @GetMapping("/search")
    SearchUserResponse search(@RequestParam(name = "input") String input,
                              @RequestParam(name = "requiredDetails", required = false, defaultValue = "") String detailsSeq) {

        if (!namingValidator.validateUserId(input) || input.isEmpty()) {
            return new SearchUserResponse(List.of());
        }

        var details = new HashSet<UserDetails>();
        for (var detailStr: detailsSeq.split(",")) {
            try {
                details.add(UserDetails.valueOf(detailStr.toUpperCase()));
            } catch (IllegalArgumentException ignore) {}
        }

        return new SearchUserResponse(userService.searchUsers(input, details));
    }
}
