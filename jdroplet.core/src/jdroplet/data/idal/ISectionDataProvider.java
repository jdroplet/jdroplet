package jdroplet.data.idal;

import jdroplet.data.model.Section;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortSectionBy;
import jdroplet.util.DataSet;


import java.util.List;

public interface ISectionDataProvider extends IDataProvider{

	void remove(Integer objectId, String type);

	void add(Integer objectId, String type, Integer[] sectionIds);

	List<Integer> getSections(Integer objectId, String type);

	DataSet<Section> getSections(Integer shopId, Integer usreId, Integer parentId,
								 String type, SortSectionBy sortBy, SortOrder sortOrder, Integer pageIndex, Integer pageSize);

	Integer exists(Integer sectionId, Integer objectId, String type);
	Integer nameExists(String type, String name);
	Integer slugExists(String type, String slug);
}
