package jdroplet.plugin;

import jdroplet.core.HttpRequest;
import jdroplet.core.HttpResponse;
import jdroplet.util.StatusData;

/**
 * Created by kuibo on 2018/7/8.
 */
public abstract class Action {

    public abstract void init();

    public abstract StatusData exec(HttpRequest request, HttpResponse response);
}