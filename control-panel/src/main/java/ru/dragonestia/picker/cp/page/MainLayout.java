package ru.dragonestia.picker.cp.page;

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
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;
import ru.dragonestia.picker.cp.annotation.ServerURL;
import ru.dragonestia.picker.cp.model.Account;
import ru.dragonestia.picker.cp.service.SecurityService;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final RoomPickerInfoResponse serverInfo;
    private final String serverUrl;
    private final Account account;
    private final boolean isAdmin;

    public MainLayout(SecurityService securityService, RoomPickerClient adminClient, @ServerURL String serverUrl) {
        this.securityService = securityService;
        this.serverInfo = adminClient.getServerInfo();
        this.serverUrl = serverUrl;
        account = securityService.getAuthenticatedAccount();
        isAdmin = securityService.hasRole("ADMIN");

        var toggle = new DrawerToggle();
        var scroller = new Scroller(createSideNav());
        scroller.setWidth(100, Unit.PERCENTAGE);

        var navLayout = new VerticalLayout(createAccountButtons(), new Hr(), scroller);
        navLayout.setPadding(false);

        addToDrawer(navLayout);
        addToNavbar(toggle, createLogo());
    }

    private Component createLogo() {
        var layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.setPadding(true);
        layout.add(new Html("<h2><u>RoomPicker!</u></h2>"));
        layout.add(new Html("<sub>" + serverInfo.version() + "</sub>"));
        return layout;
    }

    private Component createAccountButtons() {
        var layout = new VerticalLayout();
        var username = new Span(new Icon(isAdmin? VaadinIcon.USER_STAR : VaadinIcon.USER));
        username.add(account.getUsername());
        layout.add(username);

        var logoutButton = new Button("Logout", event -> securityService.logout());
        logoutButton.setWidth(100, Unit.PERCENTAGE);
        layout.add(logoutButton);

        return layout;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addItem(new SideNavItem("Nodes list", NodesPage.class, VaadinIcon.FOLDER_O.create()));
        nav.addItem(new SideNavItem("Search users", UserSearchPage.class, VaadinIcon.SEARCH.create()));
        if (isAdmin) {
            nav.addItem(new SideNavItem("Accounts", AccountsPage.class, VaadinIcon.USERS.create()));
        }
        nav.addItem(new SideNavItem("Documentation", "https://github.com/ScarletRedMan/RoomPicker", VaadinIcon.BOOK.create()));
        nav.addItem(new SideNavItem("Swagger UI", serverUrl + "/api-docs-ui", VaadinIcon.CURLY_BRACKETS.create()));
        return nav;
    }
}
