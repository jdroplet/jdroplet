package jdroplet.data.idal;

/**
 * Created by kuibo on 2018/4/24.
 */
public interface IActivityUserDataProvider extends IDataProvider {

    Integer exists(Integer shopId, Integer activityId, Integer userId);
}
