/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.ejb.Stateless;
import utils.ErrorMessages;
import utils.SuccessMessages;

/**
 *
 * @author jibbo
 */
@Stateless
public class Message {

    public enum MessageType {

        ERROR,
        SUCCESS
    }

    private int code;
    private String msg = "";
    private MessageType type;

    public Message() {
        code = 0;
        type = MessageType.ERROR;
    }

    public Message(MessageType type, int code) {
        this.type = type;
        this.code = code;
    }

    public Message(MessageType type, int code, String msg) {
        this.type = type;
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        if (type == MessageType.ERROR) {
            return ErrorMessages.getErrorMessage(code) + " " + this.msg;
        } else {
            return SuccessMessages.getSuccessMessage(code) + " " + this.msg;
        }

    }

}
