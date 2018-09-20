package jdroplet.data.idal;



/**
 * Created by kuibo on 2017/12/12.
 */
public interface IShopDataProvider extends IDataProvider {
    boolean exists(Integer userId, Integer shopId);
}
