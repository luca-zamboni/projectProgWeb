/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author luca
 */
public class Group implements Serializable{
    private int groupid;
    private String title;
    private int owner;
    private boolean priva;
    private ArrayList<Post> posts = new ArrayList();
    private ArrayList<UserBean> users = new ArrayList<>();
    private ArrayList<String> allFiles = new ArrayList<>();

    public Group(int groupid, String title, int owner, boolean priva) {
        this.groupid = groupid;
        this.title = title;
        this.owner = owner;
        this.priva = priva;
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
        this.posts.addAll(posts);
    }

    public ArrayList<UserBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserBean> users) {
        this.users.addAll(users);
    }
    
    
    
}
