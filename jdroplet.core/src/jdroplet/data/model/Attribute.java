package jdroplet.data.model;

import jdroplet.core.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Attribute 实体类
 * 2018-08-24 16:16:32 bourne
 */

public class Attribute extends Entity {

    public static class DataColumns extends Entity.DataColumnsBase {

        public static String table = "jdroplet_attributes";

        public static String id = "id";
        public static String sectionId = "section_id";
        public static String name = "name";
        public static String sequence = "sequence";
        public static String type = "type";
        public static String formType = "form_type";
        public static String placeholder = "placeholder";
        public static String reg = "reg";
        public static String unique = "unique";
        public static String searchAble = "search_able";
        public static String required = "required";
        public static String args = "args";
        public static String screeningShow = "screening_show";
        public static String defaultValue = "default_value";
        public static String length = "length";
        public static String postfix = "postfix";
        public static String warningMsg = "warning_msg";
        public static String errorMsg = "error_msg";

        public static String[] getColums() {
            return new String[]{id, sectionId, name, sequence, type, formType, placeholder, reg, "`" + unique + "`",
                    searchAble, required, args, screeningShow, defaultValue, length, postfix, warningMsg, errorMsg};
        }

        public static void fill(HttpRequest req, Attribute entity) {
            entity.id = req.getIntParameter(id);
            entity.sectionId = req.getIntParameter(sectionId);
            entity.name = req.getParameter(name);
            entity.sequence = req.getIntParameter(sequence, 0);
            entity.type = req.getParameter(type);
            entity.formType = req.getParameter(formType);
            entity.placeholder = req.getParameter(placeholder);
            entity.reg = req.getParameter(reg);
            entity.unique = req.getBooleanParameter(unique);
            entity.searchAble = req.getBooleanParameter(searchAble);
            entity.required = req.getBooleanParameter(required);
            entity.args = req.getParameter(args);
            entity.screeningShow = req.getIntParameter(screeningShow);
            entity.defaultValue = req.getParameter(defaultValue);
            entity.length = req.getIntParameter(length);
            entity.postfix = req.getParameter(postfix);
            entity.warningMsg = req.getParameter(warningMsg);
            entity.errorMsg = req.getParameter(errorMsg);
        }

        public static void fill(ResultSet rs, Attribute entity) throws SQLException {
            entity.id = rs.getInt(id);
            entity.sectionId = rs.getInt(sectionId);
            entity.name = rs.getString(name);
            entity.sequence = rs.getInt(sequence);
            entity.type = rs.getString(type);
            entity.formType = rs.getString(formType);
            entity.placeholder = rs.getString(placeholder);
            entity.reg = rs.getString(reg);
            entity.unique = rs.getBoolean(unique);
            entity.searchAble = rs.getBoolean(searchAble);
            entity.required = rs.getBoolean(required);
            entity.args = rs.getString(args);
            entity.screeningShow = rs.getInt(screeningShow);
            entity.defaultValue = rs.getString(defaultValue);
            entity.length = rs.getInt(length);
            entity.postfix = rs.getString(postfix);
            entity.warningMsg = rs.getString(warningMsg);
            entity.errorMsg = rs.getString(errorMsg);
        }

        public static Map<String, Object> getKeyValues(Attribute entity) {
            Map<String, Object> map = new HashMap<>();
            map.put(id, entity.id);
            map.put(sectionId, entity.sectionId);
            map.put(name, entity.name);
            map.put(sequence, entity.sequence);
            map.put(type, entity.type);
            map.put(formType, entity.formType);
            map.put(placeholder, entity.placeholder);
            map.put(reg, entity.reg);
            map.put("`" + unique + "`", entity.unique);
            map.put(searchAble, entity.searchAble);
            map.put(required, entity.required);
            map.put(args, entity.args);
            map.put(screeningShow, entity.screeningShow);
            map.put(defaultValue, entity.defaultValue);
            map.put(length, entity.length);
            map.put(postfix, entity.postfix);
            map.put(warningMsg, entity.warningMsg);
            map.put(errorMsg, entity.errorMsg);
            return map;
        }
    }

    //id
    private Integer id;

    /**
     * 分类ID
     */
    private Integer sectionId;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sequence;

    /**
     * 属性类别
     */
    private String type;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 属性输入框提示文字
     */
    private String placeholder;

    /**
     * 属性输入框校验正则
     */
    private String reg;

    /**
     * 值是否可重复
     */
    private Boolean unique;

    /**
     * 值是否可搜索
     */
    private Boolean searchAble;

    /**
     * 值是否必填
     */
    private Boolean required;

    /**
     * 参数
     */
    private String args = "";

    /**
     * 筛查选项中是否显示
     */
    private Integer screeningShow;

    /**
     * 属性默认值
     */
    private String defaultValue;

    /**
     * 内容最大长度
     */
    private Integer length;

    /**
     * 属性后缀
     */
    private String postfix;

    /**
     * 警告信息
     */
    private String warningMsg;

    /**
     * 错误信息
     */
    private String errorMsg;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getReg() {
        return reg;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setSearchAble(Boolean searchAble) {
        this.searchAble = searchAble;
    }

    public Boolean getSearchAble() {
        return searchAble;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getArgs() {
        return args;
    }

    public void setScreeningShow(Integer screeningShow) {
        this.screeningShow = screeningShow;
    }

    public Integer getScreeningShow() {
        return screeningShow;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public String getPostfix() {
        return postfix;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    public void setWarningMsg(String warningMsg) {
        this.warningMsg = warningMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
