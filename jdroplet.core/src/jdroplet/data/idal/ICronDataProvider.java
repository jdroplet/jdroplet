package jdroplet.data.idal;

import jdroplet.data.model.Cron;
import jdroplet.util.DataSet;

import java.util.Date;


/**
 * Created by kuibo on 2017/12/26.
 */
public interface ICronDataProvider extends IDataProvider {

    DataSet<Cron> getCrons(Integer status, Integer pageIndex, Integer pageSize);
}
