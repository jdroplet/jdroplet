package jdroplet.data.idal;

/**
 * Created by kuibo on 2018/4/7.
 */
public interface IGroupMemberDataProvider extends IDataProvider {


    boolean exists(Integer groupId, Integer userId);

    Integer getGroupCount(Integer userId);

    Integer getMemberCount(Integer groupId);

}
