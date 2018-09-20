package jdroplet.data.idal;

import jdroplet.data.model.User;
import jdroplet.util.DataSet;




public interface IUserDataProvider extends IDataProvider {
    Integer validUser(User user);

    User getUser(Integer userId, String username);

    DataSet<User> getUsers(Integer[] roleIds, String searchTerms, Integer status, Integer pageIndex, Integer pageSize);

    boolean exists(String userName);
    Integer exists2(String displayName);
}
