package jdroplet.net;

import jdroplet.exceptions.SystemException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2017/9/18.
 */
public class WebClient {

    private Map<String, String> properties = new HashMap<>();

    private String charset = "utf-8";

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public byte[] get(String url) {
        WebRequest req = null;
        WebResponse resp = null;

        req = new WebRequest();
        req.setResponseCharset(charset);
        req.setContentCharset(charset);
        req.setMethod(RequestMethod.GET);
        req.setAutoRedirect(false);

        try {
            resp = req.create(url, null, properties);
            return resp.getContent();
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }

    public String getString(String url) {
        byte[] bytes = get(url);

        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new SystemException(e.getMessage());
        }
    }

    public JSONObject getJSONObject(String url) {
        String content = getString(url);

        try {
            return new JSONObject(content);
        } catch (JSONException e) {
            throw new SystemException(e.getMessage());
        }
    }

    public byte[] post(String url, Map<String, String> params) {
        WebRequest req = null;
        WebResponse resp = null;

        req = new WebRequest();
        req.setResponseCharset(charset);
        req.setContentCharset(charset);
        req.setMethod(RequestMethod.POST);
        req.setAutoRedirect(false);

        try {
            resp = req.create(url, params);
            return resp.getContent();
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }

    public String post2(String url, Map<String, String> params) {
        byte[] bytes = post(url, params);
        String content = null;

        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new SystemException(e.getMessage());
        }
    }

    public JSONObject post3(String url, Map<String, String> params) {
        String content = post2(url, params);

        try {
            return new JSONObject(content);
        } catch (JSONException e) {
            throw new SystemException(e.getMessage());
        }
    }
}
