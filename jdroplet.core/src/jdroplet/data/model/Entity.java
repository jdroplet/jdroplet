package jdroplet.data.model;


import java.io.Serializable;

/**
 * Created by kuibo on 2017/11/7.
 */
public abstract class Entity implements Serializable {

    public abstract Integer getId();

    public static class DataColumnsBase {
        public static String id = "id";
    }
}
