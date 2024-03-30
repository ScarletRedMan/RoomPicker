package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.repository.AccountRepository;
import ru.dragonestia.picker.cp.component.AccountList;
import ru.dragonestia.picker.cp.service.SecurityService;

@RolesAllowed("ADMIN")
@PageTitle("Accounts")
@Route(value = "/admin/accounts", layout = MainLayout.class)
@RequiredArgsConstructor
public class AccountsPage extends VerticalLayout {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountsPage(SecurityService securityService) {
        accountRepository = securityService.getAuthenticatedAccount().getClient().getAccountRepository();

        add(new H2("Account management"));
        add(new AccountList(accountRepository));
    }
}
