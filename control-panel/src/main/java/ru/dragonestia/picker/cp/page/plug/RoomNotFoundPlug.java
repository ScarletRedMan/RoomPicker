package ru.dragonestia.picker.cp.page.plug;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.cp.component.NavPath;

public class RoomNotFoundPlug extends ErrorPlug implements HasErrorParameter<RoomNotFoundException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<RoomNotFoundException> errorParameter) {
        var ex = errorParameter.getException();
        var nodeId = ex.getNodeId();
        var roomId = ex.getRoomId();

        init(NavPath.toRoom(nodeId, roomId), "Error 404", ex.getMessage());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
