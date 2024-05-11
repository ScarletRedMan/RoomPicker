package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.ResponseObject;
import ru.dragonestia.picker.exception.DoesNotExistsException;
import ru.dragonestia.picker.model.account.Account;
import ru.dragonestia.picker.model.account.AccountId;
import ru.dragonestia.picker.model.account.Permission;
import ru.dragonestia.picker.service.AccountService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    List<String> listAccounts() {
        return accountService.allAccounts().stream().map(account -> account.getId().getValue()).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/target/{accountId}")
    ResponseObject.Account targetAccountDetails(@PathVariable String accountId) {
        return accountService.findAccount(AccountId.of(accountId))
                .map(ResponseObject.Account::of)
                .orElseThrow(() -> DoesNotExistsException.forAccount(AccountId.of(accountId)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    List<ResponseObject.Account> listAccountsDetails(@RequestParam List<String> id) {
        return id.stream().map(this::targetAccountDetails).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Void> createAccount(@RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam List<String> permissions) {
        var account = accountService.createNewAccount(AccountId.of(username), password);
        setPermissions(account, permissions);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/target/{accountId}")
    ResponseEntity<Void> removeAccount(@PathVariable String accountId) {
        accountService.findAccount(AccountId.of(accountId)).ifPresent(accountService::removeAccount);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/target/{accountId}/permissions")
    ResponseEntity<Void> setPermissions(@PathVariable String accountId, @RequestParam List<String> permissions) {
        var account = accountService.findAccount(AccountId.of(accountId))
                .orElseThrow(() -> DoesNotExistsException.forAccount(AccountId.of(accountId)));
        setPermissions(account, permissions);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN') || principal.username.equals(accountId)")
    @PutMapping("/target/{accountId}/password")
    ResponseEntity<?> changePassword(@PathVariable String accountId, @RequestParam String newPassword) {
        var account = accountService.findAccount(AccountId.of(accountId))
                .orElseThrow(() -> DoesNotExistsException.forAccount(AccountId.of(accountId)));
        account.setPassword(passwordEncoder.encode(newPassword));
        accountService.updateState(account);
        return ResponseEntity.ok().build();
    }

    private void setPermissions(Account account, List<String> permissions) {
        account.setAuthorities(permissions.stream().map(permission -> {
            try {
                return Permission.valueOf(permission);
            } catch (IllegalArgumentException ex) {
                throw DoesNotExistsException.forPermission(permission);
            }
        }).collect(Collectors.toSet()));
        accountService.updateState(account);
    }
}
