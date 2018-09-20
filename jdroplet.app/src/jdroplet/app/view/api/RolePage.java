package jdroplet.app.view.api;

import jdroplet.bll.Metas;
import jdroplet.bll.Roles;
import jdroplet.bll.Users;
import jdroplet.data.model.Role;
import jdroplet.data.model.User;
import jdroplet.enums.CreateUserStatus;
import jdroplet.util.I18n;
import jdroplet.util.StatusData;


import java.util.List;

/**
 * Created by kuibo on 2017/9/21.
 */
public class RolePage extends APIPage {

    public static final Integer UPDATE_RESTRICTED = 1010000;

    public void list() {
        Integer userId = request.getIntParameter("userId");
        Boolean isAdmin = request.getBooleanParameter("isAdmin", false);
        List<Role> roles = null;
        StatusData sd = null;
        User user = Users.getCurrentUser();

        if (isAdmin && !user.isAdministrator()) {
            userId = user.getId();
            roles = Roles.getRoles(userId);
        } else {
            roles = Roles.getRoles();
        }

        for(Role r:roles) {
            r.getString("manage_menu");
        }
        sd = new StatusData();
        sd.setData(roles);

        render(sd);
    }

    public void save() {
        Role role = request.getObjectParameter(Role.class);

        if (role.getId() != null && role.getId() != 0 && role.getId() <= 3) {
            Metas.save(role);
            renderJSON(UPDATE_RESTRICTED, I18n.getString(Integer.toString(UPDATE_RESTRICTED)));
        } else {
            Roles.save(role);
            renderJSON(0, "", role);
        }
    }

    public void reset() {
        String[] fields = request.getParameter("fields").split("-");
        StatusData sd = null;

        sd = new StatusData();
        for(String field:fields) {
            if ("aaaaaa".equals(field)) {
            } else {
                Integer id = request.getIntParameter("id");
                String val = request.getParameter("val");
                Metas.save(Role.class, id, field, val);
            }
        }
        render(sd);
    }

    public void remove() {
        Integer roleId = request.getIntParameter("roleId", 0);
        Integer userId = request.getIntParameter("userId", 0);


        if (roleId >5 && userId == 0)
            Roles.remove(roleId);

        Roles.removeRoles(userId, roleId);
        renderJSON(0);
    }
}
