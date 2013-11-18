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
import java.sql.Statement;
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
    public static final String DB_URL = "/home/forna/git/projectProgWeb/db.sqlite";

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
    
    public void updateGroup(int group,String newTitle,String[] users,String owner) throws SQLException{
        String sql = "UPDATE groups SET groupname = ? WHERE groupid = ?";
        PreparedStatement stm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        stm.setString(1,newTitle);
        stm.setInt(2, group);
        stm.executeUpdate();
        
        String sql2 = "UPDATE user_groups SET status = 1 WHERE groupid = ?";
        PreparedStatement stm2 = con.prepareStatement(sql2);
        stm2.setInt(1, group);
        stm2.executeUpdate();
        stm2.close();
        String sql3;
        for(String mUser : users){
            System.out.print(mUser);
            sql3 = "UPDATE user_groups SET status = 0 WHERE groupid = ? AND userid = ?";
            PreparedStatement stm3 = con.prepareStatement(sql3);
            stm3.setInt(1, group);
            stm3.setInt(2, getIdFromUser(mUser));
            System.out.print(getIdFromUser(mUser));
            stm3.executeUpdate();
            stm3.close();
        }
        String sql4 = "UPDATE user_groups SET status = 0 WHERE groupid = ? AND userid = ?";
        PreparedStatement stm4 = con.prepareStatement(sql4);
        stm4.setInt(1, group);
        stm4.setInt(2, getIdFromUser(owner));
        stm4.executeUpdate();
        stm4.close();
        
    }
    
    public void newGroup(String title, String[] users, int owner) throws SQLException{
        
        String sql = "INSERT into GROUPS(ownerid,groupname,creationdate)"
                + "VALUES (?,?,strftime('%s', 'now'))";
        PreparedStatement stm = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        stm.setInt(1,owner);
        stm.setString(2, title);
        stm.executeUpdate();
        PreparedStatement stmaux = con.prepareStatement("SELECT last_insert_rowid()");
        int groupid = -1;
        ResultSet res = stmaux.executeQuery();
        if (res.next()) {
            groupid = res.getInt(1);
        }
        res.close();
        stm.close();
        
        System.err.println(groupid);
        String sql2;
        
        for(String mUser : users){
            int aux = getIdFromUser(mUser);
            sql2 = "INSERT INTO user_groups(userid,groupid,status) VALUES (?,?,0)";
            PreparedStatement stm2 = con.prepareStatement(sql2);
            stm2.setInt(1, aux);
            stm2.setInt(2, groupid);
            stm2.executeUpdate();
            stm2.close();
        }
        sql2 = "INSERT INTO user_groups(userid,groupid,status) VALUES (?,?,0)";
        PreparedStatement stm2 = con.prepareStatement(sql2);
        stm2.setInt(1, owner);
        stm2.setInt(2, groupid);
        stm2.executeUpdate();
        stm2.close();
        
    }
    
    public boolean isInGroup(int userid,int groupid) throws SQLException{
        String sql= "select count(*) from user_groups where userid=? AND groupid=? AND status = 0";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userid);
        stm.setInt(2, groupid);
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
    
    public int getGroupOwnerById(int id) throws SQLException {
        String sql = "SELECT ownerid FROM groups WHERE groupid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
//        TODO make this shit work
//        stm.setString(1, GROUP_OWNER_ID);
//        stm.setString(2, GROUP_TABLE);
//        stm.setString(3, GROUP_ID);
        stm.setInt(1, id);
        ResultSet rs;
        rs = stm.executeQuery();
        int userId=0;
        try{
            if(rs.next()){
                userId = rs.getInt(1);
            }
        }finally{
            rs.close();
            stm.close();
        }
        return userId;
    }
    
    public ArrayList<String> getAllUSer() throws SQLException{
        String sql = "SELECT username FROM users";
        PreparedStatement stm = con.prepareStatement(sql);
        ResultSet rs;
        rs = stm.executeQuery();
        ArrayList<String> mUsers = new ArrayList<>();
        try{
            while(rs.next()){
                mUsers.add(rs.getString(1));
            }
        }finally{
            rs.close();
            stm.close();
        }
        return mUsers;
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
    
    public int getIdFromUser(String user) throws SQLException{
        String sql= "select userid from users where username = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user);
        ResultSet rs;
        rs = stm.executeQuery();
        try{
            if(rs.next()){
                return rs.getInt(1);
            }
        }finally{
            rs.close();
            stm.close();
        }
        return -1;
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
    
    public String getGroupTitleById(int id) throws SQLException{
        String sql= "select groupname from groups where groupid = ? ";
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
