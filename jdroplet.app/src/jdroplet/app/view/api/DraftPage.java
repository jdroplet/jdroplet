package jdroplet.app.view.api;

import jdroplet.bll.Drafts;
import jdroplet.bll.Users;
import jdroplet.data.model.Draft;
import jdroplet.data.model.User;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;

/**
 * Created by kuibo on 2018/5/6.
 */
public class DraftPage extends APIPage {

    public void list() {
        Integer itemId = request.getIntParameter("itemId");
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize");
        User user = Users.getCurrentUser();
        DataSet<Draft> datas = null;

        datas = Drafts.getDrafts(user.getId(), itemId, pageIndex, pageSize);
        datas = (DataSet<Draft>) PluginFactory.getInstance().applyFilters("DraftPage@list", datas, itemId);

        renderJSON(0, "", datas);
    }

    public void save() {
        Draft draft = request.getObjectParameter(Draft.class);
        User user = Users.getCurrentUser();

        draft.setUserId(user.getId());
        Drafts.save(draft);

        renderJSON(0);
    }

    public void get() {
        Integer id = request.getIntParameter("id");
        Draft draft = Drafts.getDraft(id);

        renderJSON(0, "", draft);
    }

    public void delete() {
        Integer id = request.getIntParameter("id");
        Draft draft = Drafts.getDraft(id);
        User user = Users.getCurrentUser();

        if (user.getId().equals(draft.getUserId()))
            Drafts.remove(id);
        renderJSON(0);
    }
}
