package jdroplet.app.view.admin;

import jdroplet.bll.Roles;
import jdroplet.bll.Users;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.User;
import jdroplet.util.DataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jdroplet.data.model.Order.DataColumns.platform;

/**
 * Created by kuibo on 2018/1/21.
 */
public class ManageUserPage extends ManageMasterPage {

    public void edit() {
        Integer id = request.getIntParameter("id");
        User user = null;

        if (id == 0) {
            user = new User();
        } else {
            user = Users.getUser(id);
        }

        addItem("id", id);
        addItem("displayed_user", user);
    }

    public void recv() {
        Integer userId = request.getIntParameter("id");
        Integer status = request.getIntParameter("status");
        String username = request.getParameter("userName");
        String nickname = request.getParameter("displayName");
        String roles[] = request.getParameterValues("roles");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        List<Integer> roleIds = new ArrayList<Integer>();
        User user = null;

        for (int i = 0; i < roles.length; i++) {
            roleIds.add(Integer.parseInt(roles[i]));
        }

        if (userId == 0) {
            user = new User();
        } else {
            user = Users.getUser(userId);
        }

        user.setEmail(email);
        user.setDisplayName(nickname);
        user.setPassword(password);
        user.setUserName(username);
        user.setStatus(status);

        if (userId == 0) {
            user.setRegistered(new Date());
            Users.createUser(user);
        } else {
            Integer[] rids = new Integer[roleIds.size()];
            roleIds.toArray(rids);

            Roles.removeRoles(userId);
            Roles.addUserToRoles(userId, rids);
            Users.save(user);
        }

        super.addNotices("更新成功");
        response.setRedirect(SystemConfig.getSiteUrls().getUrl("user.edit", userId));
    }

    public void list() {
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize", 20);
        DataSet<User> datas = Users.getUsers(pageIndex, pageSize);

        addItem("datas", datas);
        addItem("pageIndex", pageIndex);
        addItem("pageSize", pageSize);
    }
}
