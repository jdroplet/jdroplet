package jdroplet.data.idal;



/**
 * Created by kuibo on 2018/1/29.
 */
public interface ICouponDataProvider extends IDataProvider {

    Integer getValues(Integer userId, boolean all);
}
