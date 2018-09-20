package jdroplet.app.view.admin;

import jdroplet.bll.Crons;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Cron;
import jdroplet.util.DataSet;
import jdroplet.util.JobManager;

import java.util.Date;


/**
 * Created by kuibo on 2017/12/26.
 */
public class ManageCronPage extends ManageMasterPage {

    @Override
    public void initial() {
        super.initial();
        addItem("Crons", new Crons());
    }

    public void list() {
        Integer pageIndex = request.getIntParameter("pageIndex");
        DataSet<Cron> datas = Crons.getCrons(null, pageIndex, 20);

        addItem("pageIndex", pageIndex);
        addItem("datas", datas);
    }

    public void recv() {
        Integer id = request.getIntParameter("id");
        Integer status = request.getIntParameter("cron_status", 0);
        String name = request.getParameter("cron_name");
        String desc = request.getParameter("cron_desc");
        String refer = request.getParameter("cron_refer");
        String cron_cron = request.getParameter("cron_cron");
        Cron cron = null;
        JobManager jm = null;

        jm = JobManager.getInstance();
        if (id == null || id == 0) {

        } else {
            jm.delete(id);
        }

        cron = new Cron();
        cron.setId(id);
        cron.setName(name);
        cron.setDescription(desc);
        cron.setRefer(refer);
        cron.setCron(cron_cron);
        cron.setStatus(status);

        Crons.save(cron);

        if (status == 1)
            jm.schedule(cron.getId());

        super.addNotices("更新成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("cron.edit", cron.getId()));
    }

    public void delete() {
        Integer id = request.getIntParameter("id");

        JobManager.getInstance().delete(id);
        Crons.remove(id);

        super.addNotices("删除成功");
        SiteUrls urls = null;
        urls = SystemConfig.getSiteUrls();
        response.setRedirect(urls.getUrl("cron.list", 1));
    }
}
