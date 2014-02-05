/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Comparator;
import javax.ejb.Stateless;
import servlets.ModProfile;

/**
 *
 * @author jibbo
 */
@Stateless
public class UserBean{

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
        return ((avatar == null) ? 
                DEFAULT_IMG_PATH + "img.jpg" : 
                ModProfile.IMG_PROF_DIR + userID + "." 
                    + ModProfile.DEFAULT_EXT);
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

    public UserType convertToType(int type) {
        switch (type) {
            case 1:
                return UserType.MODERATOR;
            default:
                return UserType.SIMPLE;
        }
    }
    
    public int getTypeToInt() {
        
        if(type == UserType.MODERATOR){
            return 1;
        }else{
            return 0;
        }
        
    }

    @Override
    public String toString() {
        return "UserBean{" + "userID=" + userID + ", lastLogin=" + lastLogin + ", username=" + username + ", avatar=" + ((avatar == null) ? "" : avatar) + ", type=" + type + '}';
    }

}
