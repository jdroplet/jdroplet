package jdroplet.data.mysqidal;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.ISectionDataProvider;
import jdroplet.data.model.Entity;
import jdroplet.data.model.Section;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortSectionBy;
import jdroplet.exceptions.SystemException;
import jdroplet.util.DataSet;
import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class SectionDataProvider extends DataProvider implements
		ISectionDataProvider {

	@Override
	public Entity newEntity() {
		return new Section();
	}

	@Override
	public void fill(ResultSet rs, Entity entity) throws SQLException {
		Section.DataColumns.fill(rs, (Section) entity);
	}

	@Override
	public String getTable() {
		return Section.DataColumns.table;
	}

	@Override
	public String[] getColums() {
		return Section.DataColumns.getColums();
	}

	@Override
	public Map<String, Object> getKeyValues(Entity entity) {
		return Section.DataColumns.getKeyValues((Section) entity);
	}


	private String buildSort(SortSectionBy sortBy, SortOrder sortOrder) {
        String sort_str = "";
        String sortField = null;

        switch(sortBy.getValue()) {
        case 0:
        	sortField = "id";
        	break;
        case 2:
        	sortField = "dateCreated";
        	break;
        case 3:
        	sortField = "sortOrder";
        	break;
        }
        
        if (sortOrder.getValue() == SortOrder.ASC.getValue()) {
        	sort_str = MessageFormat.format("{0} ASC", sortField);
        } else {
        	sort_str = MessageFormat.format("{0} DESC", sortField);
        }
        
        return sort_str;
	}

	@Override
	public DataSet<Section> getSections(Integer shopId, Integer userId, Integer parentId, String type,
										SortSectionBy sortBy, SortOrder sortOrder, Integer pageIndex, Integer pageSize) {
		List<Section> sections = new ArrayList<Section>();
		DataSet<Section> datas = new DataSet<Section>();
		Section s = null;
		SQLDatabase db = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String cls1[] = null;
		String cls2[] = null;
		String clause = null;
		String orderBy = null;
		String limit = null;
		Object args[] = null;
		List<String> clauses_list = new ArrayList<String>();
		List<Object> args_list = new ArrayList<Object>();
		
		if (userId != null) {
			clauses_list.add("user_id=?");
			args_list.add(userId);
		}
		if (parentId != null) {
			clauses_list.add("parent_id=?");
			args_list.add(parentId);
		}
		if (!TextUtils.isEmpty(type)) {
			clauses_list.add("type=?");
			args_list.add(type);
		}

		if (shopId != null) {
			clauses_list.add("shop_id=?");
			args_list.add(shopId);
		}
		
		args = args_list.toArray();
		clause = StringUtils.join(clauses_list, " AND ");
		
		orderBy = buildSort(sortBy, sortOrder);
		limit = (pageIndex - 1) * pageSize + "," + pageSize;
		
		cls1 = Section.DataColumns.getColums();
		cls2 = new String[] {"COUNT(0)"};
		db = new SQLDatabase();
		
		try {
			rs1 = db.query("jdroplet_sections", cls1, clause, args, null, null, orderBy, limit);
			while (rs1.next()) {
				s = new Section();
				fill(rs1, s);
				sections.add(s);
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
        	logger.error(ex);
        	throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		datas.setObjects(sections);

		try {
			db = new SQLDatabase();
			rs2 = db.query("jdroplet_sections", cls2, clause, args, null, null, null);
			if (rs2.next()) {
				datas.setTotalRecords(rs2.getInt(1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}
		return datas;
	}

	@Override
	public List<Integer> getSections(Integer objectId, String type) {
		SQLDatabase db = null;
		ResultSet rs = null;
		String cls[] = null;
		String clause = null;
		String limit = null;
		Object args[] = null;
		List<Integer> ids = null;

		cls = new String[]{"section_id"};
		clause = "object_id=? AND type=?";
		args = new Object[]{objectId, type};
		limit = "99";

		ids = new ArrayList<>();
		db = new SQLDatabase();
		try {
			rs = db.query("jdroplet_section_objects", cls, clause, args, null, null, null, limit);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			throw new SystemException(ex.getMessage());
		} finally {
			db.close();
		}

		return ids;
	}

	@Override
	public void add(Integer objectId, String type, Integer[] sectionIds) {
		SQLDatabase db = null;
		Map<String, Object> kv = null;
		List<Map<String, Object>> kvs = null;

		kvs = new ArrayList<>();
		for(Integer id:sectionIds) {
			kv = new HashMap<>();

			kv.put("object_id", objectId);
			kv.put("type", type);
			kv.put("section_id", id);

			kvs.add(kv);
		}
		db = new SQLDatabase();
		db.insert("jdroplet_section_objects", null, kvs);
	}

	@Override
	public void remove(Integer objectId, String type) {
		SQLDatabase db = null;
		Object[] args = null;
		String clause = null;

		clause = "object_id=? AND type=?";
		args = new Object[] { objectId, type };

		db = new SQLDatabase();
		db.delete("jdroplet_section_objects", clause, args);
	}

	@Override
	public Integer exists(Integer sectionId, Integer objectId, String type) {
		return super.exists("jdroplet_section_objects", "section_id", "section_id=? and object_id=? and type=?", new Object[]{sectionId, objectId, type});
	}

	@Override
	public Integer nameExists(String type, String name) {
		return super.exists(getTable(), "type=? and name=?", new Object[]{type, name});
	}

	@Override
	public Integer slugExists(String type, String slug) {
		return super.exists(getTable(), "type=? and slug=?", new Object[]{type, slug});
	}
}
