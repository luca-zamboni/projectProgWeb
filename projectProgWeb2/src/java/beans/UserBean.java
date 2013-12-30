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

    public UserBean() {
    }

    public UserBean(int id) {
        this.userID=id;
    }
    
}
