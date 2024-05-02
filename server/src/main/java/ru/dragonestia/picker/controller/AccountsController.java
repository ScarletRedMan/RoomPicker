package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.exception.AccountDoesNotExistsException;
import ru.dragonestia.picker.api.exception.PermissionNotFoundException;
import ru.dragonestia.picker.api.model.account.ResponseAccount;
import ru.dragonestia.picker.api.repository.response.AllAccountsResponse;
import ru.dragonestia.picker.model.Account;
import ru.dragonestia.picker.model.Permission;
import ru.dragonestia.picker.service.AccountService;

import java.util.HashSet;

@Log4j2
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;

    @GetMapping("/current")
    ResponseAccount currentAccount() {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return account.toResponseObject();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountId}")
    ResponseEntity<ResponseAccount> findAccount(@PathVariable String accountId) {
        try {
            return ResponseEntity.ok(accountService.loadUserByUsername(accountId).toResponseObject());
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    AllAccountsResponse allAccounts() {
        return new AllAccountsResponse(accountService.allAccounts().stream()
                .map(Account::toResponseObject)
                .toList());
    }

    @PostMapping
    ResponseAccount registerAccount(@RequestParam String username, @RequestParam String password, @RequestParam String permissions) {
        var account = accountService.createNewAccount(username, password);

        var authorities = new HashSet<Permission>();
        for (var permStr : permissions.split(",")) {
            try {
                var perm = Permission.valueOf(permStr);
                authorities.add(perm);
            } catch (IllegalArgumentException ex) {
                throw new PermissionNotFoundException(permStr);
            }
        }
        account.setAuthorities(authorities);

        accountService.updateState(account);

        return account.toResponseObject();
    }

    @PutMapping("/{accountId}")
    ResponseEntity<?> updatePermissions(@PathVariable String accountId, @RequestParam String permissions) {
        var account = accountService.findAccount(accountId).orElseThrow(() -> new AccountDoesNotExistsException(accountId));

        var authorities = new HashSet<Permission>();
        for (var permStr : permissions.split(",")) {
            try {
                var perm = Permission.valueOf(permStr);
                authorities.add(perm);
            } catch (IllegalArgumentException ex) {
                throw new PermissionNotFoundException(permStr);
            }
        }
        account.setAuthorities(authorities);

        accountService.updateState(account);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{accountId}")
    ResponseEntity<?> removeAccount(@PathVariable String accountId) {
        var account = accountService.findAccount(accountId).orElseThrow(() -> new AccountDoesNotExistsException(accountId));
        accountService.removeAccount(account);

        return ResponseEntity.ok().build();
    }
}
