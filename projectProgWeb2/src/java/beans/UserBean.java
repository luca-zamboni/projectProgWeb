/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;

/**
 *
 * @author jibbo
 */
@Stateless
public class UserBean {

    public enum UserType {

        SIMPLE,
        MODERATOR
    }

    public static final String DEFAULT_IMG_PATH = "img/";

    private int userID;
    private long lastLogin;
    private String username;
    private String avatar;
    private UserType type;

    public UserBean() {
    }

    public UserBean(int id, long lastLogin, String username, int type) {
        this.userID = id;
        this.lastLogin = lastLogin;
        this.username = username;
        this.type = convertToType(type);
    }

    public int getUserID() {
        return userID;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return DEFAULT_IMG_PATH+ ((avatar == null) ? "default.jpg" : userID+".png");
    }

    public UserType getType() {
        return type;
    }

    public void setUserId(int uid) {
        this.userID = uid;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = convertToType(type);
    }

    private UserType convertToType(int type) {
        switch (type) {
            case 1:
                return UserType.MODERATOR;
            default:
                return UserType.SIMPLE;
        }
    }

    @Override
    public String toString() {
        return "UserBean{" + "userID=" + userID + ", lastLogin=" + lastLogin + ", username=" + username + ", avatar=" + ((avatar == null) ? "" : avatar) + ", type=" + type + '}';
    }

}
