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

    private int userid;
    private String user;
    private String text;
    private int postid;
    private long date;
    private String avatar;
    private ArrayList<String> qrs = new ArrayList();
    private ArrayList<String> files = new ArrayList();
    private int groupid;

    public Post() {
    }

    public Post(int userid,String user, String text, long date, int groupid, int postid) {
        this.userid = userid;
        this.user = user;
        this.text = text;
        this.date = date;
        this.groupid = groupid;
        this.postid = postid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
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
        this.qrs.addAll(qrs);
    }
    
    public void setQrs(String qr) {
        this.qrs.add(qr);
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
