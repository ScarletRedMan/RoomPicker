package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
