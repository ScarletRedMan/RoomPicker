package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteParameters;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.cp.exception.Unauthorized;
import ru.dragonestia.picker.cp.model.AccountSession;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;

public abstract class SecuredView extends VerticalLayout implements BeforeEnterObserver {

    private final SessionService sessionService;
    private final RouteParamExtractor paramsExtractor;
    private final AccountSession accountSession;
    private final RoomPickerClient client;
    private final boolean authenticated;

    public SecuredView(SessionService sessionService, RouteParamExtractor paramsExtractor, Permission... requiredPermissions) {
        this.sessionService = sessionService;
        this.paramsExtractor = paramsExtractor;
        this.accountSession = sessionService.getSession();

        boolean auth = true;
        try {
            checkAuth();
            checkPermissions(requiredPermissions);
        } catch (Unauthorized ex) {
            auth = false;
        }
        authenticated = auth;

        client = auth? accountSession.getClient() : null;
    }

    public final SessionService getSessionService() {
        return sessionService;
    }

    public final AccountSession getAccountSession() {
        return accountSession;
    }

    public final RoomPickerClient getClient() {
        return client;
    }

    public RouteParamExtractor getParamsExtractor() {
        return paramsExtractor;
    }

    private void checkAuth() throws Unauthorized {
        if (accountSession == null || accountSession.getData() == null) {
            throw new Unauthorized();
        }
    }

    private void checkPermissions(Permission... permissions) throws Unauthorized {
        for (var permission: permissions) {
            if (!accountSession.getData().permissions().contains(permission)) {
                throw new Unauthorized();
            }
        }
    }

    @Override
    public final void beforeEnter(BeforeEnterEvent event) {
        if (!authenticated) {
            UI.getCurrent().getPage().setLocation("/login");
            return;
        }

        preRender(event.getRouteParameters());
        render();
    }

    protected void preRender(RouteParameters routeParams) {}

    protected abstract void render();
}
