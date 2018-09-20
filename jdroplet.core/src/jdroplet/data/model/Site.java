package jdroplet.data.model;


import java.util.Date;

import jdroplet.bll.SiteManager;
import jdroplet.util.TextUtils;


public class Site extends Meta {

    private Integer id;
    private Integer userId;
    private Integer clusterId;
    private String appRoot;
    private String title;
    private String description;
    private String keywords;
    private Date dateCreated;

    public Cluster getCluster() {
        return SiteManager.getClusterBySite(id);
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        String tmp[] = appRoot.split("/");
        return tmp[tmp.length - 1];
    }

    public String getAppRoot() {
        return appRoot;
    }

    public void setAppRoot(String appRoot) {
        this.appRoot = appRoot;
    }

    public String getTheme() {
        String theme = getString("Theme");

        return TextUtils.isEmpty(theme) ? getName() : theme;
    }

    public void setTheme(String theme) {
        setValue("Theme", theme);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
