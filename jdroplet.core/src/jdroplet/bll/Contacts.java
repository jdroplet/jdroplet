package jdroplet.bll;

import jdroplet.cache.ICache;
import jdroplet.cache.RemoteCache;
import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IContactDataProvider;
import jdroplet.data.idal.IPostDataProvider;
import jdroplet.data.model.Contact;
import jdroplet.data.model.Post;
import jdroplet.enums.SortOrder;
import jdroplet.exceptions.ApplicationException;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;
import jdroplet.util.SearchQuery;
import jdroplet.util.TextUtils;

import java.rmi.Remote;
import java.text.MessageFormat;
import java.util.Date;


public class Contacts {

	public static String GROUP_KEY_CONTACTS = "CONTACTS_GROUPS_";

	public static String KEY_CONTACT = "CONTACT_";

	public static String KEY_CONTACTS = "CONTACTS_{0}_{1}_{2}_{3}";


	public static Integer save(Contact contact) {
		IContactDataProvider provider = DataAccess.instance().getContactDataProvider();

		Integer id = provider.save(contact);
		String groupkey = GROUP_KEY_CONTACTS + contact.getUserId();
		RemoteCache.clear(groupkey);
		return id;
	}

	public static boolean exists(Integer shopId, Integer activityId, Integer userId) {
		IContactDataProvider provider = DataAccess.instance().getContactDataProvider();

		return provider.exists(shopId, activityId, userId);
	}

	public static Contact getContact(Integer id) {
		IContactDataProvider provider = DataAccess.instance().getContactDataProvider();

		return (Contact) provider.getEntity(id);
	}

	public static Contact getUserDefaultContact(Integer userId) {
		IContactDataProvider provider = DataAccess.instance().getContactDataProvider();

		return provider.getUserDefaultContact(userId);
	}

	public static DataSet<Contact> getContacts(Integer shopId, Integer userId, Integer pageIndex, Integer pageSize) {
		DataSet<Contact> datas = null;
		String key = null;

		key = MessageFormat.format(KEY_CONTACTS, shopId, userId, pageIndex, pageSize);

		if (datas == null) {
			IContactDataProvider provider = (IContactDataProvider) DataAccess.instance().createProvider("ContactDataProvider");
			SearchQuery query = null;
			SearchGroup group_root, group = null;

			group_root = new SearchGroup();
			group_root.setTerm(SearchGroup.AND);

			if (shopId != null) {
				query = new SearchQuery(Contact.DataColumns.shopId, shopId);
				group_root.addQuery(query);
			}

			if (userId != null) {
				query = new SearchQuery(Contact.DataColumns.userId, userId);
				group_root.addQuery(query);
			}

			datas = (DataSet) provider.search(group_root, Contact.DataColumns.id, SortOrder.ASC, pageIndex, pageSize);
			if (datas.getObjects().size() != 0) {
				String groupkey = GROUP_KEY_CONTACTS + userId;
				RemoteCache.add(key, groupkey, datas, ICache.DAY_FACTOR);
			}
		}
		return datas;
	}

	public static void updateDefault(Integer id, boolean isDefault) {
		IContactDataProvider provider = DataAccess.instance().getContactDataProvider();
		Contact cnt = getContact(id);

		provider.update("user_id", cnt.getUserId(), "is_default", 0);
		update("is_default", id, isDefault);
	}

	private static void update(String field, Integer id, Object value) {
		IContactDataProvider provider = DataAccess.instance().getContactDataProvider();

		provider.update("id", id, field, value);
	}
}
