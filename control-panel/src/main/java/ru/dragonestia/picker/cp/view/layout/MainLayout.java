package ru.dragonestia.picker.cp.view.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;
import ru.dragonestia.picker.cp.annotation.ServerURL;
import ru.dragonestia.picker.cp.model.AccountSession;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.view.AllAccountsView;
import ru.dragonestia.picker.cp.view.AllInstancesView;
import ru.dragonestia.picker.cp.view.SearchEntityView;

public class MainLayout extends AppLayout {

    private final SessionService sessionService;
    private final String serverUrl;
    private final AccountSession session;
    private final RoomPickerInfoResponse info;
    private final boolean isAdmin;

    public MainLayout(SessionService sessionService, @ServerURL String serverURL) {
        this.sessionService = sessionService;
        this.serverUrl = serverURL;
        this.session = sessionService.getSession();

        if (session == null) {
            info = null;
            isAdmin = false;
        } else {
            info = session.getClient().getServerInfo();
            isAdmin = session.getData().permissions().contains(Permission.ADMIN);

            var toggle = new DrawerToggle();
            var scroller = new Scroller(createSideNav());
            scroller.setWidth(100, Unit.PERCENTAGE);

            var navLayout = new VerticalLayout(createAccountButtons(), new Hr(), scroller);
            navLayout.setPadding(false);

            addToDrawer(navLayout);
            addToNavbar(toggle, createLogo());
        }
    }

    private Component createLogo() {
        var layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.setPadding(true);
        layout.add(new Html("<h2><u>RoomPicker!</u></h2>"));
        layout.add(new Html("<sub>" + info.version() + "</sub>"));
        return layout;
    }

    private Component createAccountButtons() {
        var layout = new VerticalLayout();
        var username = new Span(new Icon(isAdmin? VaadinIcon.USER_STAR : VaadinIcon.USER));
        username.add(session.getData().id().getValue());
        layout.add(username);

        var logoutButton = new Button("Logout", event -> getUI().ifPresent(sessionService::logout));
        logoutButton.setWidth(100, Unit.PERCENTAGE);
        layout.add(logoutButton);

        return layout;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addItem(new SideNavItem("Instances list", AllInstancesView.class, VaadinIcon.FOLDER_O.create()));
        nav.addItem(new SideNavItem("Search entities", SearchEntityView.class, VaadinIcon.SEARCH.create()));
        if (isAdmin) {
            nav.addItem(new SideNavItem("Accounts", AllAccountsView.class, VaadinIcon.USERS.create()));
        }
        nav.addItem(new SideNavItem("Documentation", "https://github.com/ScarletRedMan/RoomPicker", VaadinIcon.BOOK.create()));
        nav.addItem(new SideNavItem("Swagger UI", serverUrl + "/api-docs-ui", VaadinIcon.CURLY_BRACKETS.create()));
        return nav;
    }
}
