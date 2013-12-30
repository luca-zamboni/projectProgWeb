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
public class Message {
    private String msg;
    
    public Message() {
    }

    public Message(String msg) {
        this.msg = msg;
    }
    
    
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.msg;
    }
    
    
    
}
