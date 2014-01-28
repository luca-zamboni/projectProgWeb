/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author luca
 */
public class Group implements Serializable{
    private int groupid;
    private String title;
    private String avatarOwner;
    private int owner;
    private String nameOwner;
    private boolean priva;
    private boolean chiuso;
    private long lastPostDate;
    private Date date;
    private int numPartecipanti;
    private int numPost;
    private ArrayList<Post> posts = new ArrayList();
    private ArrayList<UserBean> users = new ArrayList<>();
    private ArrayList<String> allFiles = new ArrayList<>();
    
    public Group() {
    }

    public Group(int groupid, String title, int owner, boolean priva) {
        this.groupid = groupid;
        this.title = title;
        this.owner = owner;
        this.priva = priva;
    }

    public boolean isChiuso() {
        return chiuso;
    }

    public void setChiuso(boolean chiuso) {
        this.chiuso = chiuso;
    }

    public String getAvatarOwner() {
        return avatarOwner;
    }

    public void setAvatarOwner(String avatarOwner) {
        this.avatarOwner = avatarOwner;
    }

    public int getNumPartecipanti() {
        return numPartecipanti;
    }

    public void setNumPartecipanti(int numPartecipanti) {
        this.numPartecipanti = numPartecipanti;
    }

    public int getNumPost() {
        return numPost;
    }

    public void setNumPost(int numPost) {
        this.numPost = numPost;
    }
    
    public String getNameOwner() {
        return nameOwner;
    }

    public void setNameOwner(String nameOwner) {
        this.nameOwner = nameOwner;
    }

    public ArrayList<String> getAllFiles() {
        return allFiles;
    }

    public void setAllFiles(ArrayList<String> allFiles) {
        this.allFiles.addAll(allFiles);
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public boolean isPriva() {
        return priva;
    }

    public void setPriva(boolean priva) {
        this.priva = priva;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
    }

    public ArrayList<UserBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserBean> users) {
        this.users.addAll(users);
    }

    public void setDate(Date date) {
        this.date=date;
    }

    public void setLastPostDate(long lastPostDate) {
        this.lastPostDate=lastPostDate;
    }

    public Date getDate() {
        return date;
    }
    
    
    
}
