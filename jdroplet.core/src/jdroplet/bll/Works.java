package jdroplet.bll;

import java.util.HashMap;
import java.util.Map;

import jdroplet.data.dalfactory.DataAccess;
import jdroplet.data.idal.IWorkDataProvider;
import jdroplet.data.model.Work;
import jdroplet.plugin.PluginFactory;
import jdroplet.util.DataSet;


public class Works {
	
	public static Work get(int id) {
		IWorkDataProvider provider = null;
		
		provider = DataAccess.instance().getWorkDataProvider();
		return provider.get(id);
	}
	
	public static Work get(int senderId, int recipientId, int itemId) {
		IWorkDataProvider provider = null;
		
		provider = DataAccess.instance().getWorkDataProvider();
		return provider.get(senderId, recipientId, itemId);
	}
	
	public static int add(Work w) {
		IWorkDataProvider provider = null;
		int id = -1;
		
		provider = DataAccess.instance().getWorkDataProvider();
		id = provider.add(w);
		
		//sendEmail(req.getRecipientId());
		
		PluginFactory.getInstance().doAction("work_add_" + w.getType(), w);
		return id;
	}

	public static void delete(int id) {
		IWorkDataProvider provider = null;

		provider = DataAccess.instance().getWorkDataProvider();

		provider.delete(id);
	}

	public static void confirmed(int id) {
		Map<String, Object> values = null;
		Work wr = null;
		
		values = new HashMap<String, Object>();
		values.put("status", 1);
		Works.update(id, values);
		
		wr = Works.get(id);
		PluginFactory.getInstance().doAction("work_confirmed_" + wr.getType(), wr);
	}
	
	private static void update(int id, Map<String, Object> values) {
		IWorkDataProvider provider = null;
		
		provider = DataAccess.instance().getWorkDataProvider();
		provider.update(id, values);
	}
	
	public static int getWorks(int userId) {
		IWorkDataProvider provider = null;

		provider = DataAccess.instance().getWorkDataProvider();

		return provider.getWorks(userId);
	}

	public static DataSet<Work> getWorks(int userId, int status, int pageIndex, int pageSize) {
		return getWorks(-1, userId, status, pageIndex, pageSize);
	}
	
	public static DataSet<Work> getWorks(int senderId,
			int recipientId, int status, int pageIndex, int pageSize) {
		IWorkDataProvider provider = null;

		provider = DataAccess.instance().getWorkDataProvider();

		return provider.getWorks(senderId, recipientId, status, pageIndex,
				pageSize);
	}
}
