package jdroplet.data.idal;


import jdroplet.data.model.Role;

import java.util.List;

public interface IRoleDataProvider extends IDataProvider {

    List<Role> getRoles(Integer userId);
    List<Role> getRoles();

    boolean exists(Integer userId, Integer roleId);

    void removeRoles(Integer roleId, Integer userId);

    void addUsersToRoles(List<Integer> userIds, Integer[] roleIds);
}
