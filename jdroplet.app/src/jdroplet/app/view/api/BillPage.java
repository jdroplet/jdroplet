package jdroplet.app.view.api;

import jdroplet.bll.Bills;
import jdroplet.bll.Users;
import jdroplet.data.model.Bill;
import jdroplet.data.model.User;
import jdroplet.util.DataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuibo on 2018/2/12.
 */
public class BillPage extends APIPage {

    public void list() {
        Integer pageIndex = request.getIntParameter("pageIndex", 1);
        Integer pageSize = request.getIntParameter("pageSize", 20);
        String type = request.getParameter("type");
        User user = Users.getCurrentUser();
        DataSet<Bill> datas = null;
        Integer userId = request.getIntParameter("userId");

        if (!user.isAdministrator() || userId == null)
            userId = user.getId();

        if ("inpour".equals(type) || "1".equals(type)) {
            datas = Bills.getBills(userId, 1, pageIndex, pageSize);
        } else {
            datas = Bills.getBills(userId, 6, pageIndex, pageSize);

            // 临时fixed
            List<Bill> newBill = new ArrayList();
            boolean exists = false;
            for(Bill billa:datas.getObjects()) {

                for(Bill billb:newBill) {
                    if (billb.getDescription().equals(billa.getDescription())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    newBill.add(billa);
                }
            }
            datas.setObjects(newBill);
        }

        renderJSON(0, "", datas);
    }

}
