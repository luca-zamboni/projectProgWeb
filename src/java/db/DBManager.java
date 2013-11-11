/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jibbo
 */
public class DBManager implements Serializable {
    
//  users
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AVATAR = "avatar"; //(file)

//  group
    public static final String GROUP_ID = "groupid";
    public static final String GROUP_NAME = "groupname";
    public static final String OWNER_ID = "ownerid";
    public static final String CREATION_DATE =  "creationdate";

//  user_group relational between group and users

//  post
    public static final String POST_ID = "postid";
    public static final String POST_DATE = "date";
    //field groupid 
    public static final String CONTENT = "text";
    //field post creator id

//  post_file
    public static final String FILE_ID = "fileid";
    public static final String FILE_CONTENT =  "file";
    //facultative (id_gruppo)
    //facultative (id_scrivente)
    // field id_post

//  invites relational between group and users, we can also add a field in 
//  the group members table with a value as "accepted", "pending", "declined", etc.

 // transient == non viene serializzato
    private static transient Connection con;
    private static final String URL_PREFIX = "jdbc:sqlite:";

    public DBManager(String dburl) throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC", true,
                    getClass().getClassLoader());
            Connection con = DriverManager.getConnection(URL_PREFIX+dburl);
            this.con = con;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }
    
    public boolean login(String user, String passwd) throws SQLException{
        String sql= "select count(*) where username=? passwd=?";
        PreparedStatement stm = con.prepareStatement("sql");
        stm.setString(1, user);
        stm.setString(2, passwd);
        ResultSet rs = stm.executeQuery();
        try{
            if(rs.next()){
                return rs.getInt(0)==1;
            }
        }finally{
            rs.close();
            stm.close();
        }
        return false;
    }
}
