package jdroplet.data.model;

public class RolesConfiguration {

    private String users = "Users";
    private String administrators = "Administrators";
    private String moderators = "Moderators";
    private String editors = "Editors";
    private String sectionsAdministrators = "SectionsAdministrators";
    private String everyone = "Everyone";

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getAdministrators() {
        return administrators;
    }

    public void setAdministrators(String administrators) {
        this.administrators = administrators;
    }

    public String getModerators() {
        return moderators;
    }

    public void setModerators(String moderators) {
        this.moderators = moderators;
    }

    public String getEditors() {
        return editors;
    }

    public void setEditors(String editors) {
        this.editors = editors;
    }

    public String getSectionsAdministrators() {
        return sectionsAdministrators;
    }

    public void setSectionsAdministrators(String sectionsAdministrators) {
        this.sectionsAdministrators = sectionsAdministrators;
    }

    public String getEveryone() {
        return everyone;
    }

    public void setEveryone(String everyone) {
        this.everyone = everyone;
    }
}
