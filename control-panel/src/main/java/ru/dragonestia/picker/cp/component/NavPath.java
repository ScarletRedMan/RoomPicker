package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NavPath extends HorizontalLayout{

    public NavPath(Point root, Point... points) {
        setWidth("100%");
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#F3F3F3")
                .set("padding-left", "1rem")
                .set("padding-right", "1rem");

        {
            var button = createPointButton(root, points.length > 0);
            button.getStyle().set("font-weight", "bold");
            add(button);
        }

        if (points.length != 0) {
            add(createDelimiterComponent());

            for (int i = 0, n = points.length; i < n; i++) {
                var button = createPointButton(points[i], i + 1 != n);

                add(button);

                if (i + 1 != n) {
                    add(createDelimiterComponent());
                }
            }
        }
    }

    private Component createDelimiterComponent() {
        return new Icon(VaadinIcon.ANGLE_RIGHT);
    }

    private Button createPointButton(Point point, boolean enable) {
        var text = new Span(point.name());

        var button = new Button(text);
        if (enable) {
            button.addClickListener(event -> {
                getUI().ifPresent(ui -> ui.navigate(point.uri()));
            });
        }
        button.getStyle()
                .setPadding("0")
                .setBorder("0");
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_LARGE);
        return button;
    }

    public record Point(String name, String uri) {}
}
