package jdroplet.data.model;

import jdroplet.annotation.db.DataTable;



@DataTable(name = "jdroplet_page_urlpatterns")
public class UrlPattern {

    private Integer id;
    private Cluster cluster;
    private Page page;
    private String action;
    private String params;
    private String description;
    private String allows;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAllows() {
        return allows;
    }

    public void setAllows(String allows) {
        this.allows = allows;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
