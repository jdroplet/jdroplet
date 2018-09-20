package jdroplet.data.idal;


import jdroplet.data.model.Entity;
import jdroplet.enums.SortOrder;
import jdroplet.util.DataSet;
import jdroplet.util.SearchGroup;


import java.util.List;

public interface IDataProvider {

    Entity getEntity(Integer id);
    Integer save(Entity entity);
    void remove(Integer id);
    void delete(Integer id);
    int update(String clauseFiled, Integer id, String field, Object value);

    DataSet<Entity> search(SearchGroup group, String sortField, SortOrder sortOrder, Integer pageIndex, Integer pageSize);
    List<Entity> search(SearchGroup group, String sortField, SortOrder sortOrder);
}
