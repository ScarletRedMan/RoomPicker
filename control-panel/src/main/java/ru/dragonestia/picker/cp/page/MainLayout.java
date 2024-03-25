package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;
import ru.dragonestia.picker.cp.annotation.ServerURL;

public class MainLayout extends AppLayout {

    private final RoomPickerInfoResponse serverInfo;
    private final String serverUrl;

    public MainLayout(RoomPickerClient adminClient, @ServerURL String serverUrl) {
        this.serverInfo = adminClient.getServerInfo();
        this.serverUrl = serverUrl;

        var toggle = new DrawerToggle();
        var scroller = new Scroller(createSideNav());

        addToDrawer(scroller);
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

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addItem(new SideNavItem("Nodes list", NodesPage.class, VaadinIcon.FOLDER_O.create()));
        nav.addItem(new SideNavItem("Search users", UserSearchPage.class, VaadinIcon.SEARCH.create()));
        nav.addItem(new SideNavItem("Documentation", "https://github.com/ScarletRedMan/RoomPicker", VaadinIcon.BOOK.create()));
        nav.addItem(new SideNavItem("Swagger UI", serverUrl + "/api-docs-ui", VaadinIcon.CURLY_BRACKETS.create()));
        return nav;
    }
}
