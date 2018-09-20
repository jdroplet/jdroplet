package jdroplet.data.idal;

import java.util.Date;

/**
 * Created by kuibo on 2018/7/3.
 */
public interface ICheckInDataProvider extends IDataProvider {

    Integer isChecked(Integer shopId, Integer userId, Date date);

}
