package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.cp.component.AccountList;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

@PageTitle("Accounts")
@Route(value = "/admin/accounts", layout = MainLayout.class)
public class AllAccountsView extends SecuredView {

    public AllAccountsView(SessionService sessionService, RouteParamExtractor paramExtractor) {
        super(sessionService, paramExtractor, Permission.ADMIN);
    }

    @Override
    protected void render() {
        add(new H2("Account details management"));
        add(new AccountList(getClient()));
    }
}
