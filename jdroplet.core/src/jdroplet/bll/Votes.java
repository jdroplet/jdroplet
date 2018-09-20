package jdroplet.bll;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IVoteDataProvider;
import jdroplet.data.model.Vote;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.Transforms;

import java.util.Date;

/**
 * Created by kuibo on 2018/2/26.
 */
public class Votes {

    public static void add(Integer userId, Integer itemId, String type) {
        IVoteDataProvider provider = DataAccess.instance().getVoteDataProvider();

        provider.save(new Vote(userId, itemId, type));
        PluginFactory.getInstance().doAction("Votes@add" + Transforms.toUpperCasePosition(type, 0), userId, itemId, type);
    }

    public static boolean isVoted(Integer userId, Integer itemId, String type) {
        boolean v = false;

        if (userId == null || userId == 0)
            return v;

        v = isVoted(userId, itemId, type, null, null);
        return v;
    }

    public static boolean isVoted(Integer userId, Integer itemId, String type, Date dateFrom, Date dateTo) {
        IVoteDataProvider provider = DataAccess.instance().getVoteDataProvider();

        return provider.getVoteCount(userId, itemId, type, dateFrom, dateTo) > 0;
    }
}
