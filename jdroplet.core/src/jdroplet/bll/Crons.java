package jdroplet.bll;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.ICronDataProvider;
import jdroplet.data.model.Cron;
import jdroplet.util.DataSet;

import java.util.Date;


/**
 * Created by kuibo on 2017/12/26.
 */
public class Crons {

    public static Integer save(Cron cron) {
        ICronDataProvider provider = DataAccess.instance()
                .getCronDataProvider();

        Integer id = null;
        id = provider.save(cron);
        if (cron.getId() == null || cron.getId() == 0) {
            cron.setId(id);
        }
        return id;
    }

    public static void remove(Integer id) {
        ICronDataProvider provider = DataAccess.instance()
                .getCronDataProvider();

        provider.remove(id);
    }


    public static DataSet<Cron> getCrons(Integer status, Integer pageIndex, Integer pageSize) {
        ICronDataProvider provider = DataAccess.instance()
                .getCronDataProvider();

        return provider.getCrons(status, pageIndex, pageSize);
    }

    public static Cron getCron(Integer id)  {
        ICronDataProvider provider = DataAccess.instance()
                .getCronDataProvider();

        return (Cron) provider.getEntity(id);
    }
    
}
