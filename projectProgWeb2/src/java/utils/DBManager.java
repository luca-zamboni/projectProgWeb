/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import beans.UserBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
//import models.Group;
//import models.Post;

/**
 * classe che si occupa dell'interfaccia con il database e di svolgere le
 * richieste al database necessarie per il funzionamento del programma
 *
 * @author jibbo
 */
public class DBManager implements Serializable {

    public static final String USER_TABLE = "users";
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String AVATAR = "avatar"; //(file)
    public static final String USERTYPE = "type";
    public static final String GROUP_TABLE = "groups";
    public static final String GROUP_ID = "groupid";
    public static final String GROUP_NAME = "groupname";
    public static final String GROUP_OWNER_ID = "ownerid";
    public static final String GROUP_CREATION_DATE = "groupcreation";
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
    public static final String FILE_CONTENT = "filepath";
    public static final String FILE_POST_ID = "postid";
    //facultative (id_gruppo)
    //facultative (id_scrivente)
    //  invites relational between group and users, we can also add a field in 
    //  the group members table with a value as "accepted", "pending", "declined", etc.
    // transient == non viene serializzato
    public static transient Connection con;
    private static final String URL_PREFIX = "jdbc:sqlite:";
    public static final String DB_URL = "database/db.sqlite";

    public DBManager(HttpServletRequest request) throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC", true,
                    getClass().getClassLoader());
            String path = request.getServletContext().getRealPath("/");
            DBManager.con = DriverManager.getConnection(URL_PREFIX + path + DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            //throw new RuntimeException(e.toString(), e);
        }
    }

    public void updateGroup(int group, String newTitle, String[] users, String owner) throws SQLException {
        String sql = "UPDATE groups SET groupname = ? WHERE groupid = ?";
        PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setString(1, newTitle);
        stm.setInt(2, group);
        stm.executeUpdate();

        ArrayList<String> usersInDb = getAllUser();
        PreparedStatement stm2;

        String sql2;
        for (String user : users) {
            int id = getIdFromUser(user);
            if (!isPending(id, group) && !isKicked(id, group) && !isInGroup(id, group)) {
                sql2 = "INSERT INTO user_groups(userid,groupid,status) VALUES (?,?,2)";
                stm2 = con.prepareStatement(sql2);
                stm2.setInt(1, id);
                stm2.setInt(2, group);
                stm2.executeUpdate();
                stm2.close();
            } else if (isKicked(id, group)) {
                sql2 = "UPDATE user_groups SET status = 2 WHERE groupid = ? AND userid = ?";
                stm2 = con.prepareStatement(sql2);
                stm2.setInt(1, group);
                stm2.setInt(2, id);
                stm2.executeUpdate();
                stm2.close();
            }
        }

        usersInDb.removeAll(Arrays.asList(users));
        for (String user : usersInDb) {
            int id = getIdFromUser(user);
            sql2 = "UPDATE user_groups SET status = 1 WHERE groupid = ? AND userid = ?";
            stm2 = con.prepareStatement(sql2);
            stm2.setInt(1, group);
            stm2.setInt(2, id);
            stm2.executeUpdate();
            stm2.close();
        }

        String sql4 = "UPDATE user_groups SET status = 0 WHERE groupid = ? AND userid = ?";
        PreparedStatement stm4 = con.prepareStatement(sql4);
        stm4.setInt(1, group);
        stm4.setInt(2, getIdFromUser(owner));
        stm4.executeUpdate();
        stm4.close();

    }

    public int newGroup(String title, String[] users, int owner) throws SQLException {

        String sql = "INSERT into GROUPS(ownerid,groupname,creationdate)"
                + "VALUES (?,?,strftime('%s', 'now'))";
        PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setInt(1, owner);
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

        String sql2;

        for (String mUser : users) {
            int aux = getIdFromUser(mUser);
            sql2 = "INSERT INTO user_groups(userid,groupid,status) VALUES (?,?,2)";
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

        insertPost(owner, groupid, "Creation of group " + title);

        return groupid;

    }

    public void newFile(int ownerid, int groupid, String nomeFile) throws SQLException {
        String sql = "INSERT INTO post_file(ownerid,groupid,filename) VALUES (?,?,?)";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, ownerid);
        stm.setInt(2, groupid);
        stm.setString(3, nomeFile);
        stm.executeUpdate();
        stm.close();
    }

    public String getOwnerPostAvatar(int owner) throws SQLException {
        String ownerA = "";
        String sql = "SELECT " + AVATAR + " FROM users WHERE userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, owner);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                ownerA = rs.getString(1);
            }
        } finally {
            rs.close();
            stm.close();
        }
        if (ownerA == null || ownerA.equals("")) {
            ownerA = "img.jpg";
        }
        return ownerA;
    }

//    public ArrayList<Post> getAllPost(int group) throws SQLException {
//        ArrayList<Post> p = new ArrayList();
//        String sql = "SELECT ownerid,date,content FROM post WHERE groupid = ?";
//        PreparedStatement stm = con.prepareStatement(sql);
//        stm.setInt(1, group);
//        ResultSet rs;
//        rs = stm.executeQuery();
//        try {
//            while (rs.next()) {
//                int own = rs.getInt(1);
//                String date = rs.getString(2);
//                String content = rs.getString(3);
//                p.add(new Post(own, date, content));
//            }
//        } finally {
//            rs.close();
//            stm.close();
//        }
//        return p;
//    }
    public void insertPost(int userid, int groupid, String post) throws SQLException {
        Date d = new Date();
        String aux = "" + d.getTime();
        String sql = "INSERT INTO post(groupid,ownerid,date,content) VALUES (?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupid);
        stm.setInt(2, userid);
        stm.setString(3, aux);
        stm.setString(4, post);
        stm.executeUpdate();
        stm.close();
    }

//    TODO finish this
//    public boolean insertGroup(String groupName, boolean prvt, int[] userIds, 
//            int ownerId) throws SQLException {
//
//        String sql = "INSERT INTO " + GROUP_TABLE + " (groupname, ownerid) "
//                + "VALUES (?,?)";
//        PreparedStatement stm = con.prepareStatement(sql);
//        stm.setString(1, groupName);
//        stm.setInt(2, ownerId);
//        int changed = stm.executeUpdate();
//        stm.close;
//        
//        String sql2 = "INSERT INTO user_groups(userid,groupid,status) VALUES (?,?,2)";
//    }

    public boolean isKicked(int userid, int groupid) throws SQLException {
        String sql = "select count(*) from user_groups where userid=? AND groupid=? AND status = 1";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userid);
        stm.setInt(2, groupid);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }
        } finally {
            rs.close();
            stm.close();
        }
        return false;
    }

    public void acceptPending(int groupid, int userid) throws SQLException {
        String sql = "UPDATE user_groups SET status = 0 WHERE groupid = ? and userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupid);
        stm.setInt(2, userid);
        stm.executeUpdate();
    }

    public void declinePending(int groupid, int userid) throws SQLException {
        String sql = "UPDATE user_groups SET status = 1 WHERE groupid = ? and userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupid);
        stm.setInt(2, userid);
        stm.executeUpdate();
    }

    public boolean isPending(int userid, int groupid) throws SQLException {
        String sql = "select count(*) from user_groups where userid=? AND groupid=? AND status = 2";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userid);
        stm.setInt(2, groupid);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }
        } finally {
            rs.close();
            stm.close();
        }
        return false;
    }

    public boolean isInGroup(int userid, int groupid) throws SQLException {
        String sql = "select count(*) from user_groups where userid=? AND groupid=? AND status = 0";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userid);
        stm.setInt(2, groupid);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }
        } finally {
            rs.close();
            stm.close();
        }
        return false;
    }

    public int getGroupOwnerById(int id) throws SQLException {
        String sql = "SELECT ownerid FROM groups WHERE groupid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs;
        rs = stm.executeQuery();
        int userId = 0;
        try {
            if (rs.next()) {
                userId = rs.getInt(1);
            }
        } finally {
            rs.close();
            stm.close();
        }
        return userId;
    }

    public String getAvatar(int userid) throws SQLException {
        String sql = "SELECT " + AVATAR + "FROM users WHERE userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userid);
        ResultSet rs;
        rs = stm.executeQuery();
        String avatarStr = "";
        try {
            if (rs.next()) {
                avatarStr = rs.getString(1);
                if (avatarStr == null || avatarStr.equals("")) {
                    avatarStr = "img.jpg";
                }
            }
        } finally {
            rs.close();
            stm.close();
        }
        return avatarStr;
    }

    public void setAvatar(String user, String extension) throws SQLException {
        String sql = "UPDATE users SET " + AVATAR + " = ? WHERE userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user + "." + extension);
        stm.setInt(2, getIdFromUser(user));
        stm.executeUpdate();
    }

    public ArrayList<String> getAllUser() throws SQLException {
        String sql = "SELECT username FROM users";
        PreparedStatement stm = con.prepareStatement(sql);
        ResultSet rs;
        rs = stm.executeQuery();
        ArrayList<String> mUsers = new ArrayList<>();
        try {
            while (rs.next()) {
                mUsers.add(rs.getString(1));
            }
        } finally {
            rs.close();
            stm.close();
        }
        return mUsers;
    }

    private String getDateOfLastPostInGroupByUser(int groupId, int userId) throws SQLException {
        String sql = "SELECT date FROM post WHERE ownerid = ?  "
                + "AND groupid = ? ORDER BY date DESC LIMIT 1;";

        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(2, groupId);
        stm.setInt(1, userId);

        ResultSet rs;
        rs = stm.executeQuery();

        String date = null;

        try {
            if (rs.next()) {
                date = rs.getString(1);
            }
        } finally {
            rs.close();
            stm.close();
        }

        return date;
    }

    public ArrayList<ArrayList<Object>> getDataForReport(int groupId) throws SQLException {
        String sql1 = "SELECT count(*), username, avatar, users.userid "
                + "FROM users, post , groups, user_groups "
                + "WHERE post.ownerid = users.userid "
                + "AND users.userid = user_groups.userid "
                + "AND user_groups.groupid = groups.groupid "
                + "AND post.groupid = ?"
                + "AND groups.groupid = ? GROUP BY users.userid";

        PreparedStatement stm = con.prepareStatement(sql1);
        stm.setInt(1, groupId);
        stm.setInt(2, groupId);

        ResultSet rs;
        rs = stm.executeQuery();

        ArrayList<ArrayList<Object>> ret = new ArrayList();

        try {
            while (rs.next()) {
                int postNum, userId;
                String userName, avatarPath;
                postNum = rs.getInt(1);
                userName = rs.getString(2);
                avatarPath = rs.getString(3);
                userId = rs.getInt(4);
                ///// 3 hardcoded change this shit
                ArrayList<Object> aux = new ArrayList();

                aux.add(0, postNum);
                aux.add(1, userName);
                aux.add(2, avatarPath);
                aux.add(3, getDateOfLastPostInGroupByUser(groupId, userId));
                ret.add(aux);
            }
        } finally {
            rs.close();
            stm.close();
        }

        return ret;
    }
//    private ArrayList<Group> getGroups(int status, String user) throws SQLException, ParseException {
//        String sql = "select groups.groupid,groupname,creationdate,groups.ownerid,post.date "
//                + "from groups,users,user_groups,post "
//                + "WHERE groups.groupid = user_groups.groupid "
//                + "AND users.userid= user_groups.userid "
//                + "AND post.groupid= groups.groupid "
//                + "AND username = ? and user_groups.status = ? "
//                + "GROUP BY groups.groupid "
//                + "ORDER by post.date DESC ";
//        PreparedStatement stm = con.prepareStatement(sql);
//        stm.setString(1, user);
//        stm.setInt(2, status);
//        ResultSet rs;
//        rs = stm.executeQuery();
//        ArrayList<Group> mGroups = new ArrayList<>();
//        try {
//            while (rs.next()) {
//                int i1, i4;
//                long l5;
//                String s2, s3, s5;
//                i1 = rs.getInt(1);
//                s2 = rs.getString(2);
//                s3 = rs.getString(3);
//                i4 = rs.getInt(4);
//                s5 = rs.getString(5);
//                l5 = Long.parseLong(s5);
//                Group aux = new Group(i1, i4, l5, s2, s3);
//                aux.setOwnerName(getUserFormId(i4));
//                mGroups.add(aux);
//            }
//        } finally {
//            rs.close();
//            stm.close();
//        }
//        return mGroups;
//    }

//    public ArrayList<Group> getAllPendingsGroups(String user) throws SQLException, ParseException {
//        return getGroups(2, user);
//    }
//
//    public ArrayList<Group> getAllGroups(String user) throws SQLException, ParseException {
//        return getGroups(0, user);
//
//    }
    public int getIdFromUser(String user) throws SQLException {
        String sql = "select userid from users where username = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            rs.close();
            stm.close();
        }
        return -1;
    }

    public String getUserFormId(int id) throws SQLException {
        String sql = "select username from users where userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getString(1);
            }
        } finally {
            rs.close();
            stm.close();
        }
        return "";
    }

    public String getGroupTitleById(int id) throws SQLException {
        String sql = "select groupname from groups where groupid = ? ";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getString(1);
            }
        } finally {
            rs.close();
            stm.close();
        }
        return "";
    }

    public String getEmail(int userId) throws SQLException {
        String sql = "select email from users where " + USERID + "=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userId);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getString(1);
            }
        } finally {
            rs.close();
            stm.close();
        }
        return null;
    }

    /**
     *
     * @return the id of the user if logged -1 otherwise
     * @throws SQLException
     */
    public UserBean login(String user, String passwd) throws SQLException {
        int userid = -1;
        long lastlogin = -1;
        int type = -1;

        String sql = "select " + USERID + " , " + USERTYPE + " from users where username=? AND password=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user);
        stm.setString(2, passwd);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                userid = rs.getInt(1);
                type = rs.getInt(2);
            }
        } finally {
            rs.close();
            stm.close();
        }

        sql = "SELECT last_login FROM user_login WHERE id_user=? ORDER BY last_login DESC";
        stm = con.prepareStatement(sql);
        stm.setInt(1, getIdFromUser(user));
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                lastlogin = Long.parseLong(rs.getString(1));
            }
        } finally {
            rs.close();
            stm.close();
        }

        sql = "INSERT into user_login (id_user,last_login) VALUES (?,?)";
        stm = con.prepareStatement(sql);
        stm.setInt(1, getIdFromUser(user));
        stm.setString(2, "" + new Date().getTime());
        stm.executeUpdate();

        return new UserBean(userid, lastlogin, user, type);
    }

    public boolean insertUser(String user, String passwd, String email) throws SQLException {
        String sql = "insert into " + USER_TABLE + "(" + USERNAME + "," + PASSWORD + "," + EMAIL + ") values(?,?,?)";
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement(sql);
            stm.setString(1, user);
            stm.setString(2, passwd);
            stm.setString(3, email);
            stm.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
        return false;
    }

    public void setNewForgetPass(int user, String code, String newPass, String scadenza) {
        try {
            String sql = "INSERT INTO forget_pass (new_pass,code,user,scadenza) VALUES (?,?,?,?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, newPass);
            stm.setString(2, code);
            stm.setInt(3, user);
            stm.setString(4, scadenza);
            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long getScadenza(String code) {
        try {
            String sql = "SELECT scadenza FROM forget_pass WHERE code = ?";
            PreparedStatement stm = null;
            long scade = 0;

            stm = con.prepareStatement(sql);
            stm.setString(1, code);

            ResultSet rs = stm.executeQuery();
            try {
                if (rs.next()) {
                    String scad = rs.getString(1);
                    scade = Long.parseLong(scad);
                }
            } finally {
                rs.close();
                stm.close();
            }
            return scade;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public boolean setNewPassword(String code) {
        try {
            int user = -1;
            String newPass = "";
            String sql = "select user,new_pass from forget_pass where code = ? order by scadenza desc";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, code);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = rs.getInt(1);
                newPass = rs.getString(2);
            }
            rs.close();
            stm.close();

            if (user == -1) {
                return false;
            }

            sql = "UPDATE users SET password = ? WHERE userid = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, newPass);
            stm.setInt(2, user);
            stm.executeUpdate();

            sql = "DELETE from forget_pass WHERE code = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, code);
            stm.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Acciderbolina qualcosa è andato storto\n");
        }
        return false;
    }

    public void setPassword(int id, String passwd) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, passwd);
        stm.setInt(2, id);
        stm.executeUpdate();
    }
}
