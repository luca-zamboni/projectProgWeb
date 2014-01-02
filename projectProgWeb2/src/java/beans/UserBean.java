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
    private int userID;
    private int lastLogin;

    public UserBean() {
    }

    public UserBean(int id,int lastLogin) {
        this.userID=id;
        this.lastLogin=lastLogin;
    }
    
    public int getUserID(){
        return userID;
    }
    
    public int getLastLogin(){
        return lastLogin;
    }
    
}
