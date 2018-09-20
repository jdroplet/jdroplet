package jdroplet.app.view.api;

import jdroplet.bll.Statistics;
import jdroplet.data.model.Statistic;
import jdroplet.util.DataSet;

import java.util.Date;

/**
 * Created by kuibo on 2018/8/22.
 */
public class StatisticPage extends APIPage {

    public void list() {
        Integer groupId = request.getIntParameter("groupId");
        Integer pageIndex = request.getIntParameter("pageIndex", 1);
        Integer pageSize = request.getIntParameter("pageSize", 9999);
        Date dateFrom = request.getDateParameter("dateFrom");
        Date dateTo = request.getDateParameter("dateTo");
        DataSet<Statistic> datas = null;

        datas = Statistics.getStatistics(groupId, dateFrom, dateTo, pageIndex, pageSize);
        renderJSON(0, null, datas);
    }
}
