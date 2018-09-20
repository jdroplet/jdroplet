package jdroplet.data.model;

import jdroplet.core.HttpContext;
import jdroplet.util.TextUtils;

import java.util.*;

/**
 * Created by kuibo on 2017/12/12.
 */
public class Menu {

    private int sort;
    private String key;
    private String action;
    private String name;
    private String url;
    private String icon;
    private String group;
    private boolean isAtive;
    private LinkedHashMap<String, Menu> subMenus;

    public Menu() {
        sort = 10;
        name = "";
        url = "";
        icon = "";
        group = "";
        subMenus = new LinkedHashMap<>();
    }

    public boolean isAtive() {
        return isAtive;
    }

    public void setAtive(boolean ative) {
        isAtive = ative;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkedHashMap<String, Menu> getSubMenus() {
        return subMenus;
    }

    public void addMenu(String key, Menu menu) {
        subMenus.put(key, menu);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
