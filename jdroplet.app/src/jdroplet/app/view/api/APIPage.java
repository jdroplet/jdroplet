package jdroplet.app.view.api;

import jdroplet.core.HttpContext;
import jdroplet.core.Page;
import jdroplet.enums.ActionStatus;
import jdroplet.util.StatusData;
import jdroplet.util.TextUtils;

/**
 * Created by kuibo on 2017/10/13.
 */
public class APIPage extends Page {

    protected void preInit(HttpContext context) {
        this.context = context;
        this.request = context.request();
        this.response = context.response();

        setContentType("application/json; charset=" + this.context.request().getEncoding());
    }

    @Override
    public void initial() {
        isCustomContent = true;

        response.setHeader("access-control-allow-credentials", "true");
        response.setHeader("access-control-allow-headers", "Content-Type, Authorization, X-Requested-With");
        response.setHeader("access-control-allow-methods", "PUT,POST,GET,DELETE,OPTIONS");

        if (TextUtils.isEmpty(request.getHeader("Origin"))) {
            response.setHeader("access-control-allow-origin", "*");
        } else {
            response.setHeader("access-control-allow-origin", request.getHeader("Origin"));
        }
    }

    public void showError(Throwable error) {
        StatusData sd = null;

        sd = new StatusData();
        sd.setStatus(ActionStatus.ERROR.getValue());
        sd.setMsg(error.getMessage());

        render(sd);
    }

    protected void handleOptionMethod() {
        render("OK");
    }
}
