package jdroplet.bll;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IDraftDataProvider;
import jdroplet.data.model.Draft;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;

/**
 * Created by kuibo on 2018/5/6.
 */
public class Drafts {

    public static Draft getDraft(Integer id) {
        IDraftDataProvider provider = (IDraftDataProvider) DataAccess.instance().createProvider("DraftDataProvider");

        return (Draft) provider.getEntity(id);
    }

    public static void remove(Integer id) {
        IDraftDataProvider provider = (IDraftDataProvider) DataAccess.instance().createProvider("DraftDataProvider");

        provider.remove(id);
    }

    public static Integer save(Draft draft) {
        IDraftDataProvider provider = (IDraftDataProvider) DataAccess.instance().createProvider("DraftDataProvider");
        Integer id = null;

        id = provider.save(draft);
        if (draft.getId() == null || draft.getId() == 0)
            draft.setId(id);
        return id;
    }

    public static Integer getDrafts(Integer itemId) {
        IDraftDataProvider provider = (IDraftDataProvider) DataAccess.instance().createProvider("DraftDataProvider");

        return provider.getDrafts(itemId);
    }

    public static DataSet<Draft> getDrafts(Integer userId, Integer itemId, Integer pageIndex, Integer pageSize) {
        DataSet<Draft> datas = null;

        if (datas == null) {
            IDraftDataProvider provider = (IDraftDataProvider) DataAccess.instance().createProvider("DraftDataProvider");
            SearchQuery query = null;
            SearchGroup group_root, group = null;

            group_root = new SearchGroup();
            group_root.setTerm(SearchGroup.AND);

            if (userId != null && userId != 0) {
                query = new SearchQuery(Draft.DataColumns.userId, userId);
                group_root.addQuery(query);
            }

            if (itemId != null && itemId != 0) {
                query = new SearchQuery(Draft.DataColumns.itemId, itemId);
                group_root.addQuery(query);
            }

            datas = (DataSet) provider.search(group_root, Draft.DataColumns.id, SortOrder.DESC, pageIndex, pageSize);
        }
        return datas;
    }
}
