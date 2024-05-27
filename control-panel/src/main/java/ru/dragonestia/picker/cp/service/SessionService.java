package ru.dragonestia.picker.cp.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.cp.exception.Unauthorized;
import ru.dragonestia.picker.cp.model.AccountSession;

@Component
public class SessionService {

    public void setSession(AccountSession session) {
        VaadinSession.getCurrent().setAttribute(AccountSession.class, session);
    }

    public AccountSession getSession() {
        return VaadinSession.getCurrent().getAttribute(AccountSession.class);
    }

    public void checkAuthorisation() throws Unauthorized {
        if (VaadinSession.getCurrent().getAttribute(AccountSession.class) == null) {
            throw new Unauthorized();
        }
    }

    public void login(AccountSession session, UI ui) {
        setSession(session);
        ui.getPage().setLocation("/");
    }

    public void logout(UI ui) {
        VaadinSession.getCurrent().setAttribute(AccountSession.class, null);
        ui.getPage().setLocation("/login");
    }

    public boolean hasPermission(Permission permission) {
        return getSession().getData().permissions().contains(permission);
    }
}
