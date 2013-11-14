/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author luca
 */
public class Group {
    
    int id;
    int owner;
    String groupName;
    String CreationDate;
    String ownername;

    public int getId() {
        return id;
    }

    public Group(int id, int owner, String groupName, String CreationDate) {
        this.id = id;
        this.owner = owner;
        this.groupName = groupName;
        this.CreationDate = CreationDate;
    }

    public String getOwnerName() {
        return ownername;
    }
    
    public void setOwnerName(String ow){
        ownername = ow;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String CreationDate) {
        this.CreationDate = CreationDate;
    }
    
    
}
