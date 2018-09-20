package jdroplet.data.mysqidal;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdroplet.data.dalfactory.SQLDatabase;
import jdroplet.data.idal.IContactDataProvider;
import jdroplet.data.model.Contact;
import jdroplet.data.model.Entity;
import jdroplet.exceptions.ApplicationException;
import jdroplet.exceptions.SystemException;
import jdroplet.util.DataSet;

import jdroplet.util.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ContactDataProvider extends DataProvider implements IContactDataProvider {

	@Override
	public Entity newEntity() {
		return new Contact();
	}

	@Override
	public void fill(ResultSet rs, Entity entity) throws SQLException {
		Contact.DataColumns.fill(rs, (Contact) entity);
	}

	@Override
	public String getTable() {
		return Contact.DataColumns.table;
	}

	@Override
	public String[] getColums() {
		return Contact.DataColumns.getColums();
	}

	@Override
	public Map<String, Object> getKeyValues(Entity entity) {
		return Contact.DataColumns.getKeyValues((Contact) entity);
	}

	@Override
	public boolean exists(Integer shopId, Integer activityId, Integer userId) {
		return super.exists(getTable(), "id", "shop_id=? AND activity_id=? AND user_id=?", new Object[]{shopId, activityId, userId}) != null;
	}

	@Override
	public Contact getUserDefaultContact(Integer userId) {
		Contact cnt = new Contact();

		fillEntity("user_id = ? and is_default = 1", new Object[]{userId}, null, cnt);
		return cnt;
	}
}
