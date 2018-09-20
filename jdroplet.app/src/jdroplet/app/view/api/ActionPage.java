package jdroplet.app.view.api;

import jdroplet.enums.ActionStatus;
import jdroplet.plugin.Action;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.StatusData;

/**
 * Created by kuibo on 2018/3/18.
 */
public class ActionPage extends APIPage {

    public void invoke() {
        String name = request.getParameter("_act");
        Action act = null;
        StatusData sd = null;

        try {
            act = (Action) PluginFactory.getInstance().applyFilters("ActionPage@invoke" + name, null, name);
            sd = act.exec(request, response);
        } catch(Exception ex) {
            sd = new StatusData();
            sd.setStatus(ActionStatus.ERROR.getValue());
            sd.setMsg(ex.getMessage());
        }

        render(sd);
    }
}
