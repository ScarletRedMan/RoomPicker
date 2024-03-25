package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.model.account.ResponseAccount;
import ru.dragonestia.picker.model.Account;
import ru.dragonestia.picker.service.AccountService;

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
}
