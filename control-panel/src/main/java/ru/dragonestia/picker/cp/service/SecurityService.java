package ru.dragonestia.picker.cp.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.cp.model.Account;

@Service
@RequiredArgsConstructor
public class SecurityService {

    public Account getAuthenticatedAccount() {
        var context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication().getPrincipal() instanceof Account account) {
            return account;
        }
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation("/login");
        var logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }
}
