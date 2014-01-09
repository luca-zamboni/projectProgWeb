/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 * classe che rappresenta il post
 * @author luca
 */
public class Post {
    int owner;
    String creationdate;
    String text;

    public Post(int owner, String creationdate, String text) {
        this.owner = owner;
        this.creationdate = creationdate;
        this.text = text;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
