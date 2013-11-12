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
    
    public static final String USER_TABLE = "users";
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AVATAR = "avatar"; //(file)

    public static final String GROUP_TABLE = "group";
    public static final String GROUP_ID = "groupid";
    public static final String GROUP_NAME = "groupname";
    public static final String GROUP_OWNER_ID = "ownerid";
    public static final String GROUP_CREATION_DATE =  "groupcreation";

    public static final String RELATION_USER_GROUP = "user_group";
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
    public static final String FILE_CONTENT =  "file";
    public static final String FILE_POST_ID = "postid";
    //facultative (id_gruppo)
    //facultative (id_scrivente)

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
