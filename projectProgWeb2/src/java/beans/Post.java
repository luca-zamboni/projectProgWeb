/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;
import utils.Support;

/**
 *
 * @author luca
 */
public class Post {
    private String userid;
    private String text;
    private int postid;
    private long date;
    private ArrayList<String> qrs = new ArrayList();
    private ArrayList<String> files = new ArrayList();
    private int groupid;

    public Post(String userid, String text, long date, int groupid, int postid) {
        this.userid = userid;
        this.text = text;
        this.date = date;
        this.groupid = groupid;
        this.postid = postid;
    }
    
    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return Support.getDateFromTime(date);
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<String> getQrs() {
        return qrs;
    }

    public void setQrs(ArrayList<String> qrs) {
        this.qrs = qrs;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }
    
    
    
}
