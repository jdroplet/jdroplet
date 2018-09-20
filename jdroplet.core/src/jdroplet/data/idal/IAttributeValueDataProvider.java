package jdroplet.data.idal;


import jdroplet.data.model.AttributeValue;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;

import java.util.List;

public interface IAttributeValueDataProvider extends IDataProvider {

    DataSet<Integer> getItemIds(List<AttributeValue> avs, SearchGroup group, String type, Integer pageIndex, Integer pageSize);
}
