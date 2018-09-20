package jdroplet.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet<T> implements Serializable {
    private static final long serialVersionUID = -4038858332094845486L;

    private List<T> objects = null;
    private int totalRecords = 0;
    private Map<String, Object> data = null;

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public boolean hasResults() {
        if (objects != null && objects.size() > 0)
            return true;
        return false;
    }

    public void add(String key, Object obj) {
        if (data == null)
            data = new HashMap<>();

        data.put(key, obj);
    }
}
