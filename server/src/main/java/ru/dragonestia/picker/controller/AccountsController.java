package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.ResponseObject;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    List<String> listAccounts() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/target/{accountId}")
    ResponseObject.Account targetAccountDetails(@PathVariable String accountId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    ResponseObject.Account listAccountsDetails(@RequestParam List<String> id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Void> createAccount(@RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam List<String> permissions) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/target/{accountId}")
    ResponseEntity<Void> removeAccount(@PathVariable String accountId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/target/{accountId}/permissions")
    ResponseEntity<Void> setPermissions(@PathVariable String accountId,
                                        @RequestParam List<String> permissions) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN') || principal.username.equals(accountId)")
    @PutMapping("/target/{accountId}/password")
    ResponseEntity<?> changePassword(@PathVariable String accountId, @RequestParam String newPassword) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
