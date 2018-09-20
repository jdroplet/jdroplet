package jdroplet.sns.utils.wechat;

import jdroplet.cache.RemoteCache;
import jdroplet.exceptions.AuthorizationException;
import jdroplet.exceptions.SystemException;
import jdroplet.net.WebClient;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kuibo on 2017/9/18.
 */
public class TemplateMessage {

    protected String appId;
    protected String secret;
    protected String token;
    protected Map<String, Object> data;

    public TemplateMessage(String appId, String secret, String recvOpenId, String templateId, String url) {
        this.appId = appId;
        this.secret = secret;
        this.data = new HashMap<String, Object>();

        this.data.put("touser", recvOpenId);
        this.data.put("template_id", templateId);
        this.data.put("url", url);
    }

    public Map<String, Map> buildMsg(Map<String, String> msgData) {
        Map<String, Map> dataItem = null;
        Map<String, String> dataRow = null;

        dataItem = new HashMap<String, Map>();

        Iterator<Map.Entry<String, String>> it = msgData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();

            dataRow = new HashMap<>();
            dataRow.put("value", entry.getValue());
            dataRow.put("color", "#173177");
            dataItem.put(entry.getKey(), dataRow);
        }

        return dataItem;
    }

    public void send(Map<String, Map> msgData) {
        String url = null;
        WebClient client = null;
        JSONObject json = null;
        Map<String, String> params = null;
        String tokenCacheKey = null;

        tokenCacheKey = appId + "_token";
        this.token = (String) RemoteCache.get(tokenCacheKey);
        if (TextUtils.isEmpty(this.token)) {
            this.token = WeChatHelp.getToken(this.appId, this.secret);

            RemoteCache.add(tokenCacheKey, this.token, 6000);
        }
        this.data.put("data", msgData);

        params = new HashMap<>();
        params.put("", JSONUtil.toJSONString(this.data));
        url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + this.token;
        client = new WebClient();
        json = client.post3(url, params);

        if (json.optInt("errcode") != 0) {  //异常情况
            if (json.optInt("errcode") == 40001) {
                throw new AuthorizationException("code:" + json.optInt("errcode") + " msg:" + json.optString("errmsg"));
            } else {
                throw new SystemException("code:" + json.optInt("errcode") + " msg:" + json.optString("errmsg") + " recv:" + this.data.get("touser"));
            }
        }
    }
}
