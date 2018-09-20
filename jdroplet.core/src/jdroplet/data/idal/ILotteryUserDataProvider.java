package jdroplet.data.idal;

/**
 * Created by kuibo on 2018/4/23.
 */
public interface ILotteryUserDataProvider extends IDataProvider {

    Integer exists(Integer activityId, Integer userId, Integer itemId, Integer status);
}
