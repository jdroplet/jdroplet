package jdroplet.data.idal;

import jdroplet.data.model.Visit;

/**
 * Created by kuibo on 2018/8/21.
 */
public interface IVisitDataProvider extends IDataProvider {

    Visit getLast(String user);
}
