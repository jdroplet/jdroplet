package jdroplet.bll;

import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IRoleDataProvider;
import jdroplet.data.model.Role;
import jdroplet.util.JSONUtil;

import java.util.ArrayList;
import java.util.List;

public class Roles {

    public static Integer save(Role role) {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();
        Integer id = null;
        id = provider.save(role);
        if (role.getId() == null || role.getId() == 0) {
            role.setId(id);
        }
        Metas.save(role);
        Logs.save("save-role-" + role.getId(), JSONUtil.toJSONString(role));
        return id;
    }

    public static Role getRole(Integer id) {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();
        return (Role) provider.getEntity(id);
    }

    public static List<Role> getRoles() {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();

        return provider.getRoles();
    }

    public static List<Role> getRoles(Integer userId) {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();

        return provider.getRoles(userId);
    }

    public static String[] getUserRoleNames(Integer userId) {
        String[] rolesname = null;
        List<Role> roles = null;

        roles = getRoles(userId);
        rolesname = new String[roles.size()];
        for (int idx = 0; idx < roles.size(); idx++)
            rolesname[idx] = roles.get(idx).getName();

        return rolesname;
    }

    public static boolean exists(Integer userId, Integer roleId) {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();

        return provider.exists(userId, roleId);
    }

    public static void addUserToRole(Integer userId, Integer roleId) {
        ArrayList<Integer> userIds = new ArrayList<Integer>();

        userIds.add(userId);

        addUsersToRole(userIds, roleId);
    }

    public static void addUserToRoles(Integer userId, Integer[] roleIds) {
        ArrayList<Integer> userIds = new ArrayList<Integer>();

        userIds.add(userId);

        addUsersToRoles(userIds, roleIds);
    }

    public static void addUsersToRole(List<Integer> userIds, Integer roleId) {
        addUsersToRoles(userIds, new Integer[]{roleId});
    }

    public static void addUsersToRoles(List<Integer> userIds, Integer[] roleIds) {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();
        provider.addUsersToRoles(userIds, roleIds);

        Logs.save("save-roles", JSONUtil.toJSONString(userIds) + JSONUtil.toJSONString(roleIds));
        RemoteCache.clear(Users.GROUP_USER);
    }

    public static void removeRoles(Integer userId) {
        removeRoles(userId, null);
    }

    public static void removeRoles(Integer userId, Integer roleId) {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();
        provider.removeRoles(userId, roleId);

        Logs.save("remove-roles", "userId:" + userId + "-roleId:" + roleId);
        RemoteCache.clear(Users.GROUP_USER);
    }

    public static void remove(Integer id) {
        IRoleDataProvider provider = DataAccess.instance().getRoleDataProvider();
        provider.remove(id);

        Metas.remove(Role.class, id);
        Logs.save("remove-role-" + id, "");
        RemoteCache.clear(Users.GROUP_USER);
    }
}
