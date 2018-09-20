package jdroplet.app.view.api;

import jdroplet.bll.Contacts;
import jdroplet.bll.Posts;
import jdroplet.bll.Users;
import jdroplet.data.model.Contact;
import jdroplet.data.model.User;
import jdroplet.util.DataSet;
import jdroplet.util.I18n;
import jdroplet.util.StatusData;

public class ContactPage extends APIPage {

	public static final Integer INVALID_ID = 1009000;

	public void get() {
		Integer id = null;
		User user = null;
		Contact cnt = null;

		user = Users.getCurrentUser();
		id = request.getIntParameter("id", 0);

		if (id == 0) {
			cnt = Contacts.getUserDefaultContact(user.getId());
			renderJSON(0, null, cnt);
		} else {
			cnt = Contacts.getContact(id);
			if (cnt.getUserId().equals(user.getId())) {
				renderJSON(0, null, cnt);
			} else {
				renderJSON(INVALID_ID, I18n.getString(Integer.toString(INVALID_ID)));
			}
		}
	}

	public void save() {
		User user = Users.getCurrentUser();
		Contact contact = request.getObjectParameter(Contact.class);;
		Contact raw = Contacts.getContact(contact.getId());

		if (raw.getUserId().equals(user.getId())) {
			contact.setUserId(user.getId());
			Contacts.save(contact);
			renderJSON(0);
		} else {
			renderJSON(INVALID_ID, I18n.getString(Integer.toString(INVALID_ID)));
		}
	}
	
	public void list() {
		Integer pageIndex = request.getIntParameter("pageIndex");
		Integer pageSize = request.getIntParameter("pageSize");
		Integer userId = request.getIntParameter("userId");
		Integer shopId = request.getIntParameter("shopId");
		User user = Users.getCurrentUser();
		String term = request.getParameter("term");
		DataSet<Contact> datas = null;
		StatusData sd = null;

		if (userId == null) {
			if (!user.isAdministrator())
				userId = user.getId();
		}
		datas = Contacts.getContacts(shopId, userId, pageIndex, pageSize);
		sd = new StatusData();
		sd.setData(datas);
		render(sd);
	}

	public void reset() {
		String[] fields = request.getParameter("fields").split("-");
		StatusData sd = null;
		User user = Users.getCurrentUser();

		sd = new StatusData();
		for(String field:fields) {
			if ("default".equals(field)) {
				Integer id = request.getIntParameter("id");
				Boolean isDefault = request.getBooleanParameter("isdefault");

				Contacts.updateDefault(id, isDefault);
			}
		}
		render(sd);
	}
}
