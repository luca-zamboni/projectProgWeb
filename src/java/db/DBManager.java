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
import java.util.ArrayList;
import models.Group;

/**
 *
 * @author jibbo
 */
public class DBManager implements Serializable {
    
    public static final String USER_TABLE = "users";
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AVATAR = "avatar"; //(file)

    public static final String GROUP_TABLE = "groups";
    public static final String GROUP_ID = "groupid";
    public static final String GROUP_NAME = "groupname";
    public static final String GROUP_OWNER_ID = "ownerid";
    public static final String GROUP_CREATION_DATE =  "groupcreation";

    public static final String RELATION_USER_GROUP = "user_groups";
    public static final String RELATION_USER_GROUP_ID = "id";
    // field userid
    //fiel groupid
    public static final String RELATION_USER_GROUP_STATUS = "status";

    public static final String POST_TABLE = "post";
    public static final String POST_ID = "postid";
    public static final String POST_DATE = "date";
    public static final String POST_GROUP_ID = "groupid"; 
    public static final String POST_CONTENT = "content";
    public static final String POST_OWNER = "ownerid";

    public static final String FILE_TABLE = "post_file";
    public static final String FILE_ID = "fileid";
    public static final String FILE_CONTENT =  "filepath";
    public static final String FILE_POST_ID = "postid";
    //facultative (id_gruppo)
    //facultative (id_scrivente)

//  invites relational between group and users, we can also add a field in 
//  the group members table with a value as "accepted", "pending", "declined", etc.

 // transient == non viene serializzato
    public static transient Connection con;
    private static final String URL_PREFIX = "jdbc:sqlite:";

    public DBManager(String dburl) throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC", true,
                    getClass().getClassLoader());
            DBManager.con = DriverManager.getConnection(URL_PREFIX+dburl);
            System.out.print(URL_PREFIX+dburl);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e.toString(), e);
        }
        
    }
    
    public ArrayList<Group> getAllGroups(String user) throws SQLException{
        String sql= "select groups.groupid,groupname,creationdate,ownerid "
                + "from groups,users,user_groups "
                + "WHERE groups.groupid = user_groups.groupid "
                + "AND users.userid= user_groups.userid "
                + "AND username = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user);
        ResultSet rs;
        rs = stm.executeQuery();
        ArrayList<Group> mGroups = new ArrayList<>();
        try{
            while(rs.next()){
                int i1,i4;
                String s2,s3;
                i1 = rs.getInt(1);
                s2 = rs.getString(2);
                s3 = rs.getString(3);
                i4 = rs.getInt(4);
                Group aux = new Group(i1, i4, s2, s3);
                aux.setOwnerName(getUserFormId(i4));
                mGroups.add(aux);
            }
        }finally{
            rs.close();
            stm.close();
        }
        return mGroups;
        
    }
    
    public String getUserFormId(int id) throws SQLException{
        String sql= "select username from users where userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1,id);
        ResultSet rs;
        rs = stm.executeQuery();
        try{
            if(rs.next()){
                return rs.getString(1);
            }
        }finally{
            rs.close();
            stm.close();
        }
        return "";
    }
    
    public boolean login(String user, String passwd) throws SQLException{
        String sql= "select count(*) from users where username=? AND password=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user);
        stm.setString(2, passwd);
        ResultSet rs;
        rs = stm.executeQuery();
        try{
            if(rs.next()){
                return rs.getInt(1)==1;
            }
        }finally{
            rs.close();
            stm.close();
        }
        return false;
    }
}
