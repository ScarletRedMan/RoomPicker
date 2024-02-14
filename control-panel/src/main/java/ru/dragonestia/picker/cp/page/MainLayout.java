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
import org.springframework.beans.factory.annotation.Qualifier;
import ru.dragonestia.picker.api.repository.RoomPickerRepository;
import ru.dragonestia.picker.api.repository.response.RoomPickerInfoResponse;

import java.net.URI;

public class MainLayout extends AppLayout {

    private final RoomPickerInfoResponse info;
    private final String serverUrl;

public MainLayout(RoomPickerRepository roomPickerRepository, URI serverUrl) {
        info = roomPickerRepository.getInfo();
        this.serverUrl = serverUrl.toString();

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
        layout.add(new Html("<sub>" + info.version() + "</sub>"));
        return layout;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addItem(new SideNavItem("Nodes list", NodesPage.class, VaadinIcon.FOLDER_O.create()));
        nav.addItem(new SideNavItem("Search users", UserSearchPage.class, VaadinIcon.SEARCH.create()));
        nav.addItem(new SideNavItem("Documentation", "https://github.com/ScarletRedMan/RoomPicker", VaadinIcon.BOOK.create()));
        nav.addItem(new SideNavItem("Swagger UI", serverUrl + "api-docs-ui", VaadinIcon.CURLY_BRACKETS.create()));
        nav.addItem(new SideNavItem("Sign-out", HomePage.class, VaadinIcon.SIGN_OUT.create()));
        return nav;
    }
}
