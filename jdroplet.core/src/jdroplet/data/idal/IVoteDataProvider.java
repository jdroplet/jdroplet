package jdroplet.data.idal;

import java.util.Date;

/**
 * Created by kuibo on 2018/2/26.
 */
public interface IVoteDataProvider extends IDataProvider {

    Integer getVoteCount(Integer userId, Integer itemId, String type, Date dateFrom, Date dateTo);
}
