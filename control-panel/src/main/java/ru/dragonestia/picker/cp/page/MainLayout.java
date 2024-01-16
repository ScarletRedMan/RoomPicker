package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class MainLayout extends AppLayout {

    public MainLayout() {
        var toggle = new DrawerToggle();
        var scroller = new Scroller(createSideNav());

        addToDrawer(scroller);
        addToNavbar(toggle, createLogo());
    }

    private Component createLogo() {
        var layout = new HorizontalLayout();
        layout.setPadding(true);
        layout.add(new Html("<h2><u>RoomPicker!</u></h2>"));
        return layout;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addItem(new SideNavItem("Nodes list", NodesPage.class, VaadinIcon.FOLDER_O.create()));
        nav.addItem(new SideNavItem("Search users", HomePage.class, VaadinIcon.SEARCH.create()));
        nav.addItem(new SideNavItem("Documentation", "https://github.com/ScarletRedMan/RoomPicker", VaadinIcon.BOOK.create()));
        nav.addItem(new SideNavItem("Sign-out", HomePage.class, VaadinIcon.SIGN_OUT.create()));
        return nav;
    }
}
