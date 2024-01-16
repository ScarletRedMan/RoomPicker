package ru.dragonestia.picker.cp.page.plug;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.picker.cp.component.NavPath;

public abstract class ErrorPlug extends VerticalLayout {

    public void init(NavPath path, String title, String description) {
        add(path);
        add(new H1(title));
        add(new Html("<p>" + description + "</p>"));
    }
}
