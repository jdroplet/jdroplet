package jdroplet.data.idal;

public interface IGroupDataProvider extends IDataProvider {

    boolean exists(Integer id);
    Integer getCount(Integer activityId, Integer userId);
}
