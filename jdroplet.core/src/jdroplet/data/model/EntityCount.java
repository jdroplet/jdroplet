package jdroplet.data.model;

import java.io.Serializable;

/**
 * Created by kuibo on 2018/2/22.
 */
public class EntityCount implements Serializable {

    private Integer id;
    private Integer count;
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
