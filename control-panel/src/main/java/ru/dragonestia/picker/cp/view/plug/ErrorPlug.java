package ru.dragonestia.picker.cp.view.plug;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.ParentLayout;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

@ParentLayout(MainLayout.class)
public abstract class ErrorPlug extends VerticalLayout {

    public void init(String title, String description) {
        add(new H1(title));
        add(new Html("<p>" + description + "</p>"));
    }
}
