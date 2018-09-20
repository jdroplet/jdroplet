package jdroplet.data.idal;

import java.util.Map;

import jdroplet.data.model.Work;
import jdroplet.util.DataSet;


public interface IWorkDataProvider {

	Work get(int id);

	Work get(int senderId, int recipientId, int itemId);

	int add(Work w);

	void delete(int id);

	void update(int id, Map<String, Object> values);

	int getWorks(int userId);
	DataSet<Work> getWorks(int senderId, int recipientId, int status,
			int pageIndex, int pageSize);
}
