package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.cp.component.RefreshableTable;
import ru.dragonestia.picker.cp.repository.dto.EntityDTO;
import ru.dragonestia.picker.cp.repository.graphql.SearchEntity;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

import java.util.LinkedList;
import java.util.List;

@PageTitle("Search entities")
@Route(value = "/entities", layout = MainLayout.class)
public class SearchEntityView extends SecuredView implements RefreshableTable {

    private TextField fieldEntityname;
    private Grid<EntityDTO> entityGrid;
    private Span foundEntities;
    private List<EntityDTO> cachedEntities = new LinkedList<>();

    @Autowired
    public SearchEntityView(SessionService sessionService, RouteParamExtractor paramExtractor) {
        super(sessionService, paramExtractor);
    }

    private TextField createEntitynameInputField() {
        var field = new TextField();
        field.setLabel("Entityname");
        field.setPlaceholder("some-entity-identifier");
        field.setRequired(true);
        field.setMinWidth(30, Unit.PERCENTAGE);
        field.setAutofocus(true);

        var button = new Button(new Icon(VaadinIcon.SEARCH));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().set("color", "#FFFFFF");
        button.addClickListener(event -> refresh());
        button.addClickShortcut(Key.ENTER);

        field.setSuffixComponent(button);
        return field;
    }

    private Grid<EntityDTO> createEntityGrid() {
        var grid = new Grid<EntityDTO>();

        grid.addColumn(EntityDTO::getId).setHeader("Identifier").setSortable(true)
                .setFooter(foundEntities);

        grid.addColumn(EntityDTO::getCountRooms).setComparator((entity1, entity2) -> {
            var r1 = entity1.getCountRooms();
            var r2 = entity2.getCountRooms();

            return Integer.compare(r1, r2);
        }).setTextAlign(ColumnTextAlign.CENTER).setHeader("Linked with rooms");

        grid.addComponentColumn(entity -> {
            var button = new Button("Details");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> {
                getUI().ifPresent(ui -> ui.navigate("/entities/" + entity.getId()));
            });
            return button;
        }).setTextAlign(ColumnTextAlign.END).setFrozenToEnd(true).setHeader(createRefreshButton());

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    private void search(String input) {
        if (input.isEmpty()) {
            entityGrid.setItems();
        }

        entityGrid.setItems(cachedEntities = getClient().getRestTemplate().executeGraphQL(SearchEntity.query(input)).getSearchEntity().stream().map(entity -> (EntityDTO) entity).toList());
    }

    @Override
    public void refresh() {
        search(fieldEntityname.getValue().trim());
        foundEntities.setText("Found %s entities".formatted(cachedEntities.size()));
    }

    public void justRefresh() {
        entityGrid.setItems();
        foundEntities.setText("Found %s entities".formatted(cachedEntities.size()));
    }

    @Override
    protected void render() {
        foundEntities = new Span();
        add(fieldEntityname = createEntitynameInputField());
        add(entityGrid = createEntityGrid());
        justRefresh();
    }
}
