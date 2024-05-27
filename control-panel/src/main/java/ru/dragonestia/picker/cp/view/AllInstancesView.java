package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.InstanceList;
import ru.dragonestia.picker.cp.component.RegisterInstance;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

@PageTitle("Instances")
@RouteAlias(value = "/", layout = MainLayout.class)
@Route(value = "/instances", layout = MainLayout.class)
public class AllInstancesView extends SecuredView {

    private InstanceList instanceList;

    public AllInstancesView(SessionService sessionService, RouteParamExtractor paramExtractor) {
        super(sessionService, paramExtractor);
    }

    protected RegisterInstance createRegisterInstanceElement() {
        return new RegisterInstance(instance -> {
            try {
                getClient().getInstanceRepository().createInstance(InstanceId.of(instance.getId()), instance.getMethod(), instance.isPersist());
                return new RegisterInstance.Response(false, "");
            } catch (Exception ex) {
                return new RegisterInstance.Response(true, ex.getMessage());
            } finally {
                instanceList.refresh();
            }
        });
    }

    protected InstanceList createInstanceListElement() {
        return new InstanceList(getClient());
    }

    @Override
    protected void render() {
        add(NavPath.rootInstances());

        if (getSessionService().hasPermission(Permission.NODE_MANAGEMENT)) {
            add(createRegisterInstanceElement());
        }

        add(new Hr());
        add(instanceList = createInstanceListElement());
    }
}
