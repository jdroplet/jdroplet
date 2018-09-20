package jdroplet.app.view.api;

import jdroplet.bll.Users;
import jdroplet.bll.Votes;
import jdroplet.data.model.User;

/**
 * Created by kuibo on 2018/2/26.
 */
public class VotePage extends APIPage {

    public void add() {
        Integer itemId = request.getIntParameter("itemId");
        String type = request.getParameter("type");
        User user = Users.getCurrentUser();

        if (Votes.isVoted(user.getId(), itemId, type)) {
            renderJSON(1, "您已经点过赞", null);
        } else {
            Votes.add(user.getId(), itemId, type);
            renderJSON(0, "点赞成功", null);
        }
    }
}
