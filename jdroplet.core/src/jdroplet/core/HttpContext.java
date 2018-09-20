package jdroplet.core;


import java.util.HashMap;
import java.util.Map;

import jdroplet.cache.ICache;
import jdroplet.security.GenericPrincipal;
import jdroplet.util.TextUtils;


public class HttpContext {
    private static ThreadLocal<HttpContext> contexts = new ThreadLocal<HttpContext>();
    private HttpRequest request;
    private HttpResponse response;
    private GenericPrincipal user;
    private Map<String, Object> items;

    public HttpContext(HttpRequest request, HttpResponse response) {
        this.setRequest(request);
        this.setResponse(response);
        this.items = new HashMap<String, Object>();

        HttpContext.setContext(this);
    }

    void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpRequest request() {
        return this.request;
    }

    void setResponse(HttpResponse response) {
        this.response = response;
    }

    public HttpResponse response() {
        return this.response;
    }

    public void setItem(String key, Object value) {
        this.items.put(key, value);
    }

    public Object getItem(String key) {
        return this.items.get(key);
    }

    public void removeItem(String key) {
        this.items.remove(key);
    }

    public String getUsername() {
        return this.user.getIdentity().getName();
    }

    public GenericPrincipal getUser() {
        return user;
    }

    public void setUser(GenericPrincipal user) {
        this.user = user;
    }

    public ICache cache() {
        return HttpRuntime.cache();
    }

    protected static void setContext(HttpContext context) {
        contexts.set(context);
    }

    public static HttpContext current() {
        return contexts.get();
    }

    public String getAppRootUrl() {
        String serverName = request.getServerName();
        String applicationPath = request.getContextPath();

        return serverName + applicationPath;
    }

    public String getSiteRootUrl() {
        return getAppRootUrl() + "/" + request.getSite();
    }

    public String getCurrentUrl() {
        String scheme = null;
        scheme = request.getHeader("X-Forwarded-Scheme");
        if (TextUtils.isEmpty(scheme))
            scheme = SystemConfig.getProperty("app.scheme@" + request.getServerName());
        if (TextUtils.isEmpty(scheme))
            scheme = request.getScheme();
        return scheme + "://" + getAppRootUrl() + request.getServletPath();
    }
}
