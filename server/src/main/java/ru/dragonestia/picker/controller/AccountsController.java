package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.model.account.ResponseAccount;
import ru.dragonestia.picker.api.repository.response.AllAccountsResponse;

@Log4j2
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

    @GetMapping("/current")
    ResponseAccount currentAccount() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountId}")
    ResponseEntity<ResponseAccount> findAccount(@PathVariable String accountId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping
    AllAccountsResponse allAccounts() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping
    ResponseAccount registerAccount(@RequestParam String username, @RequestParam String password, @RequestParam(defaultValue = "") String permissions) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PutMapping("/{accountId}")
    ResponseEntity<?> updatePermissions(@PathVariable String accountId, @RequestParam(defaultValue = "") String permissions) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/{accountId}")
    ResponseEntity<?> removeAccount(@PathVariable String accountId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PreAuthorize("hasRole('ADMIN') || principal.username.equals(accountId)")
    @PutMapping("/{accountId}/password")
    ResponseEntity<?> changePassword(@PathVariable String accountId, @RequestParam String newPassword) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
