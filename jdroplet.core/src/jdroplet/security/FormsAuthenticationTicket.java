package jdroplet.security;


import jdroplet.bll.Users;
import jdroplet.data.model.User;

import java.util.Date;

public class FormsAuthenticationTicket {
    int version;
    boolean isPersistent;
    String userName;
    String userData;
    Date issueDate;
    Date expiration;

    public FormsAuthenticationTicket(int version, String userName, Date issueDate, Date expiration,
                                     boolean isPersistent, String userData) {
        this.version = version;
        this.userName = userName;
        this.issueDate = issueDate;
        this.expiration = expiration;
        this.isPersistent = isPersistent;
        this.userData = userData;
    }

    public int getVersion() {
        return version;
    }

    public boolean isPersistent() {
        return isPersistent;
    }

    public String getName() {
        return userName;
    }

    public String getUserData() {
        return userData;
    }

    void setUserData(String data) {
        userData = data;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getSign() {
        User user = null;
        String sign = null;

        user = Users.getUser(userName);
        if (user == null)
            return "";

        sign = version + userName + issueDate.toString() + expiration.toString()
                + isPersistent + userData + user.getPassword().substring(8, 12);
        sign = DigestUtil.MD5(sign);
        return sign;
    }

    public boolean isExpired() {
        return (new Date()).getTime() > expiration.getTime();
    }

}
