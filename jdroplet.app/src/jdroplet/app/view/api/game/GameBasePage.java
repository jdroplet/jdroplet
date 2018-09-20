package jdroplet.app.view.api.game;

import jdroplet.app.view.api.APIPage;
import jdroplet.core.DateTime;
import jdroplet.security.DigestUtil;

/**
 * Created by kuibo on 2018/6/19.
 */
public class GameBasePage extends APIPage {

    public static final Integer TOKEN_VERIFY = 0;
    public static final Integer TOKEN_EXPIRE = 1;
    public static final Integer TOKEN_ERROR = 2;

    protected int getExpire() {
        return 20;
    }

    protected String getToken(Integer shopId, Integer activityId, Integer userId, long timestamp) {
        String token = null;

        token = new StringBuilder().append(shopId).append("-")
                .append(activityId).append("-").append(userId).append("-").append(timestamp).toString();

        token = DigestUtil.MD5(token);
        return token;
    }

    protected int verifyToken(int shopId, int activityId, int userId, long timestamp, String token) {
        DateTime pre_date = new DateTime(timestamp);
        DateTime cur_date = DateTime.now();
        String ticker = null;

        // 令牌失效
        if ( DateTime.diff(cur_date, pre_date) >= getExpire() )
            return TOKEN_EXPIRE;

        ticker = getToken(shopId, activityId, userId, timestamp);
        return ticker.equals(token) ? TOKEN_VERIFY : TOKEN_ERROR;
    }
}
