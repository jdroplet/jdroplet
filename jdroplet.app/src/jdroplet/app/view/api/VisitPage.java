package jdroplet.app.view.api;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jdroplet.bll.Users;
import jdroplet.bll.Visits;
import jdroplet.data.model.User;
import jdroplet.data.model.Visit;
import jdroplet.util.DataSet;
import jdroplet.util.I18n;
import jdroplet.util.IPAddress;
import jdroplet.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuibo on 2018/8/21.
 */
public class VisitPage extends APIPage {

    public static Integer RES_IS_EMPTY = 1012000;

    public void recv() {
        Visit last = null;
        Visit visit = null;
        User sysUser = Users.getCurrentUser();
        String user = null;
        Browser browser = null;
        OperatingSystem os = null;
        UserAgent ua = null;

        visit = request.getObjectParameter(Visit.class);
        visit.setIp(request.getXRemoteAddr());
        visit.setUserAgent(request.getHeader("User-Agent").toLowerCase());
        visit.setCreateTime(new Date());
        if (TextUtils.isEmpty(visit.getResource())) {
            renderJSON(RES_IS_EMPTY, I18n.getString(Integer.toString(RES_IS_EMPTY)));
            return;
        }

        if (Users.isLoggedIn(sysUser)) {
            user = sysUser.getUserName();
        } else {
            user = "guest-" + Users.getTempUser().getUserName();
        }

        try{last = Visits.getLast(user);} catch (Exception ex) {}
        if (last == null || !visit.getResource().equals(last.getResource())) {
            visit.setUser(user);

            ua = UserAgent.parseUserAgentString(visit.getUserAgent());
            browser = ua.getBrowser();
            os = ua.getOperatingSystem();

            visit.setPlatform(os.getName());
            visit.setBrowser(browser.getName());
            visit.setRegion("");
            visit.setCity("");
            visit.setIsp("");

            Visits.save(visit);
        }

        renderJSON(0);
    }

    public void list() {
        String user = request.getParameter("user");
        Integer pageIndex = request.getIntParameter("pageIndex", 1);
        Integer pageSize = request.getIntParameter("pageSize", 20);
        DataSet<Visit> datas = null;

        datas = Visits.getVisits(user, pageIndex, pageSize);
        renderJSON(0, null, datas);
    }
}
