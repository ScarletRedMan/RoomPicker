package ru.dragonestia.picker.cp.page.plug;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.cp.component.NavPath;

public class InvalidRoomIdentifierPlug extends ErrorPlug implements HasErrorParameter<InvalidRoomIdentifierException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<InvalidRoomIdentifierException> errorParameter) {
        var ex = errorParameter.getException();
        var nodeId = ex.getNodeId();
        var roomId = ex.getRoomId();

        init(NavPath.toRoom(nodeId, roomId), "Error 400", ex.getMessage());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
