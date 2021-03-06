/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import beans.Group;
import beans.Post;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringEscapeUtils;
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
    public static final String GROUP_CREATION_DATE = "creationdate";
    public static final String GROUP_PVTFLAG = "private";

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

    public int updateGroup(int group, String newTitle, List<Integer> users,
            int owner, boolean chiuso, boolean privato) throws SQLException {
        int rowChanged;
        String sql = "UPDATE groups SET groupname = ? WHERE groupid = ?";
        PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setString(1, newTitle);
        stm.setInt(2, group);
        stm.executeUpdate();

        stm.close();

        sql = "UPDATE groups SET private = ? WHERE groupid = ?";
        stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int priv = calculatePrivateColumnInt(chiuso, privato);
        stm.setInt(1, priv);
        stm.setInt(2, group);
        stm.executeUpdate();

        stm.close();

        ArrayList<Integer> usersInDb = getAllUserIDs();
        PreparedStatement stm2;

        String sql2;
        for (int id : users) {
            //int id = getIdFromUser(user);
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

        usersInDb.removeAll(users);

        for (int id : usersInDb) {
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
        stm4.setInt(2, owner);
        rowChanged = stm4.executeUpdate();
        stm4.close();
        return rowChanged;

    }

    public int newGroup(String title, String[] users, int owner,
            boolean isPrivate) throws SQLException {

        String sql = "INSERT into GROUPS(ownerid,groupname,creationdate,private)"
                + "VALUES (?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setInt(1, owner);
        stm.setString(2, title);
        stm.setString(3, "" + new Date().getTime());
        stm.setInt(4, isPrivate ? 0 : 1); //se e' privato setta a 1 altrimenti 0
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

        if (users != null) {
            sql2 = "INSERT INTO user_groups(userid,groupid,status) VALUES (?,?,2)";
            for (String mUser : users) {
                int aux = getIdFromUser(mUser);
                PreparedStatement stm2 = con.prepareStatement(sql2);
                stm2.setInt(1, aux);
                stm2.setInt(2, groupid);
                stm2.executeUpdate();
                stm2.close();
            }
        }
        sql2 = "INSERT INTO user_groups(userid,groupid,status) VALUES (?,?,0)";
        PreparedStatement stm2 = con.prepareStatement(sql2);
        stm2.setInt(1, owner);
        stm2.setInt(2, groupid);
        stm2.executeUpdate();
        stm2.close();

        String g = StringEscapeUtils.escapeHtml(title);
        insertPost(owner, groupid, "Creation of group " + g);

        return groupid;

    }

    public ArrayList<UserBean> getMembers(int groupid) throws SQLException {
        ArrayList<UserBean> us = getAllUser();
        ArrayList<UserBean> ret = new ArrayList<>();
        for (UserBean a : us) {
            if (isInGroup(a.getUserID(), groupid)) {
                ret.add(a);
            }

        }
        return ret;
    }
    
    public ArrayList<UserBean> getAllUsersInGroup(int groupid) throws SQLException {
        ArrayList<UserBean> us = getAllUser();
        ArrayList<UserBean> ret = new ArrayList<>();
        for (UserBean a : us) {
            if (isInGroup(a.getUserID(), groupid)||isPending(a.getUserID(), groupid)) {
                ret.add(a);
            }

        }
        return ret;
    }

    public void newFile(int ownerid, int postid, String nomeFile) throws SQLException {
        String sql = "INSERT INTO post_file(ownerid,postid,filename) VALUES (?,?,?)";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, ownerid);
        stm.setInt(2, postid);
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

    public ArrayList<String> getAllFileInPost(int postid) throws SQLException {
        ArrayList<String> ret = new ArrayList<>();
        String sql = "select filename from post_file where postid=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, postid);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            while (rs.next()) {
                ret.add(rs.getString(1));
            }
        } finally {
            rs.close();
            stm.close();
        }
        return ret;
    }

    public ArrayList<Post> getAllPost(int group) throws SQLException {
        ArrayList<Post> p = new ArrayList();
        String sql = "SELECT ownerid,date,content,postid FROM post WHERE groupid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, group);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            while (rs.next()) {
                int ow = rs.getInt(1);
                String us = getUserFormId(ow);
                String d = rs.getString(2);
                long date = Long.parseLong(d);
                String content = rs.getString(3);
                int postid = rs.getInt(4);
                p.add(new Post(ow, us, content, date, group, postid));
            }
        } finally {
            rs.close();
            stm.close();
        }
        return p;
    }

    public boolean isPrivateGroup(int groupid) throws SQLException {
        String sql = "select private from groups where groupid=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupid);
        ResultSet rs;
        rs = stm.executeQuery();
        try {
            if (rs.next()) {
                return rs.getInt(1)%2 == 0;
            }
        } finally {
            rs.close();
            stm.close();
        }
        return false;
    }

    public boolean isModerator(int userid) throws SQLException {
        boolean mod = false;

        String sql = "SELECT type FROM users WHERE userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userid);
        ResultSet rs = stm.executeQuery();

        try {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } finally {
            rs.close();
            stm.close();
        }

        return mod;
    }

    public boolean isModerator(String user) throws SQLException {
        boolean mod = false;

        String sql = "SELECT type FROM users WHERE username = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user);
        ResultSet rs = stm.executeQuery();

        try {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } finally {
            rs.close();
            stm.close();
        }

        return mod;
    }

    public int insertPost(int userid, int groupid, String post) throws SQLException {
        Date d = new Date();
        String aux = "" + d.getTime();
        System.err.println(post);
        String sql = "INSERT INTO post(groupid,ownerid,date,content) VALUES (?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setInt(1, groupid);
        stm.setInt(2, userid);
        stm.setString(3, aux);
        stm.setString(4, post);
        stm.executeUpdate();
        stm.close();
        PreparedStatement stmaux = con.prepareStatement("SELECT last_insert_rowid()");
        int postid = -1;
        ResultSet res = stmaux.executeQuery();
        if (res.next()) {
            postid = res.getInt(1);
        }
        res.close();
        stm.close();

        return postid;
    }

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
        String sql = "SELECT " + AVATAR + " FROM users WHERE userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, userid);
        ResultSet rs;
        rs = stm.executeQuery();
        String avatarStr = null;
        try {
            if (rs.next()) {
                avatarStr = rs.getString(1);
                if (avatarStr != null && avatarStr.equals("")) {
                    avatarStr = null;
                }
            }
        } finally {
            rs.close();
            stm.close();
        }
        return avatarStr;
    }

    public void setAvatar(int userID, String path) throws SQLException {
        String sql = "UPDATE users SET " + AVATAR + " = ? WHERE userid = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, path);
        stm.setInt(2, userID);
        stm.executeUpdate();
    }

    public ArrayList<UserBean> getAllUser() throws SQLException {
        String sql = "SELECT username, userid, avatar FROM users";
        PreparedStatement stm = con.prepareStatement(sql);
        ResultSet rs;
        rs = stm.executeQuery();
        ArrayList<UserBean> mUsers = new ArrayList<>();
        try {
            UserBean tmp;
            while (rs.next()) {
                tmp = new UserBean();
                tmp.setUserName(rs.getString(1));
                tmp.setUserId(rs.getInt(2));
                tmp.setAvatar(rs.getString(3));
                mUsers.add(tmp);
            }
        } finally {
            rs.close();
            stm.close();
        }
        return mUsers;
    }

    public ArrayList<Integer> getAllUserIDs() throws SQLException {
        String sql = "SELECT " + USERID + " FROM users";
        PreparedStatement stm = con.prepareStatement(sql);
        ResultSet rs;
        rs = stm.executeQuery();
        ArrayList<Integer> mUsers = new ArrayList<>();
        try {
            UserBean tmp;
            while (rs.next()) {
                mUsers.add(rs.getInt(1));
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

    public ArrayList<Group> getGroups() throws SQLException {
        ArrayList<Group> mGroups = new ArrayList();

        String sql = "SELECT groups.groupid, groupname, creationdate, groups.ownerid, post.date, groups.private "
                + "FROM groups, post WHERE groups.groupid = post.groupid "
                + "GROUP BY groups.groupid "
                + "ORDER BY post.date DESC";
        PreparedStatement stm = con.prepareStatement(sql);
        ResultSet rs = stm.executeQuery();

        try {
            while (rs.next()) {

                int i1, i4, i6;
                String s2;
                Date s3, s5;
                i1 = rs.getInt(1);
                s2 = rs.getString(2);
                long d = Long.parseLong(rs.getString(3));
                s3 = new Date();
                s3.setTime(d);
                i4 = rs.getInt(4);
                d = Long.parseLong(rs.getString(5));
                s5 = new Date();
                s5.setTime(d);
                i6 = rs.getInt(6);

                Group aux = new Group();

                aux.setGroupid(i1);
                aux.setTitle(s2);
                aux.setDate(s3);
                aux.setOwner(i4);
                aux.setLastPostDate(s5);
                aux.setPriva(i6 % 2 == 0);
                aux.setNumPost(getNumPost(i1));
                aux.setNumPartecipanti(getNumPartecipanti(i1));

                mGroups.add(aux);
            }
        } finally {
            rs.close();
            stm.close();
        }

        return mGroups;
    }

    public ArrayList<Group> getGroups(int status, String user) throws SQLException, ParseException {
        String sql = "select groups.groupid,groupname,creationdate,groups.ownerid,post.date,groups.private "
                + "from groups,users,user_groups,post "
                + "WHERE groups.groupid = user_groups.groupid "
                + "AND users.userid= user_groups.userid "
                + "AND post.groupid= groups.groupid "
                + "AND username = ? AND user_groups.status = ? AND (groups.private =0 OR groups.private=2)  "
                + "GROUP BY groups.groupid "
                + "ORDER by post.date DESC ";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, user);
        stm.setInt(2, status);
        ResultSet rs;
        rs = stm.executeQuery();
        ArrayList<Group> mGroups = new ArrayList<>();
        try {
            while (rs.next()) {
                int i1, i4, i6;
                String s2;
                Date s3, s5;
                i1 = rs.getInt(1);
                s2 = rs.getString(2);
                long d = Long.parseLong(rs.getString(3));
                s3 = new Date();
                s3.setTime(d);
                i4 = rs.getInt(4);
                d = Long.parseLong(rs.getString(5));
                s5 = new Date();
                s5.setTime(d);
                i6 = rs.getInt(6);

                Group aux = new Group();

                aux.setGroupid(i1);
                aux.setTitle(s2);
                aux.setDate(s3);
                aux.setOwner(i4);
                aux.setLastPostDate(s5);
                aux.setPriva(i6 % 2 == 0);
                aux.setNumPost(getNumPost(i1));
                aux.setNumPartecipanti(getNumPartecipanti(i1));

                mGroups.add(aux);
            }

            if (status != 2) {
                sql = "SELECT groups.groupid,groupname,creationdate,groups.ownerid,post.date,groups.private "
                        + "FROM groups,post "
                        + "WHERE post.groupid = groups.groupid "
                        + "AND (groups.private = 1 OR groups.private = 3) "
                        + "GROUP BY groups.groupid "
                        + "ORDER by post.date DESC ";

                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();

                while (rs.next()) {
                    int i1, i4, i6;
                    String s2;
                    Date s3, s5;
                    i1 = rs.getInt(1);
                    s2 = rs.getString(2);
                    long d = Long.parseLong(rs.getString(3));
                    s3 = new Date();
                    s3.setTime(d);
                    i4 = rs.getInt(4);
                    d = Long.parseLong(rs.getString(5));
                    s5 = new Date();
                    s5.setTime(d);
                    i6 = rs.getInt(6);

                    Group aux = new Group();

                    aux.setGroupid(i1);
                    aux.setTitle(s2);
                    aux.setDate(s3);
                    aux.setOwner(i4);
                    aux.setLastPostDate(s5);
                    aux.setPriva(i6 % 2 == 0);
                    aux.setNumPost(getNumPost(i1));
                    aux.setNumPartecipanti(getNumPartecipanti(i1));

                    mGroups.add(aux);
                }
            }

        } finally {
            rs.close();
            stm.close();
        }
        Collections.sort(mGroups);
        return mGroups;
    }

    public ArrayList<Group> getAllPendingsGroups(String user) throws SQLException, ParseException {
        return getGroups(2, user);
    }

    public ArrayList<Group> getAllGroups(String user) throws SQLException, ParseException {
        return getGroups(0, user);
    }

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

    public int getIdFromMail(String mail) throws SQLException {
        String sql = "select userid from users where email = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, mail);
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
        String sql = "insert into " + USER_TABLE + "(" + USERNAME + "," + PASSWORD + "," + EMAIL + ", type ) values(?,?,?,?)";
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement(sql);
            stm.setString(1, user);
            stm.setString(2, passwd);
            stm.setString(3, email);
            stm.setInt(4, 1);
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

    public Group fillGroupById(int groupId, boolean users, boolean posts, boolean files) throws SQLException {
        Group ret = new Group();
        ret.setGroupid(groupId);

        String sql = "SELECT ownerid, groupname, creationdate, private FROM groups WHERE groupid=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupId);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            ret.setOwner(rs.getInt(1));
            ret.setTitle(rs.getString(2));
            ret.setDate(rs.getDate(3));
            ret.setPriva(rs.getInt(4) % 2 == 0);
        }

        if (users) {
            ret.setUsers(this.getMembers(groupId));
        }

        if (posts) {
            ret.setPosts(this.getAllPost(groupId));

            sql = "SELECT date FROM post WHERE groupid=? ORDER BY date LIMIT 1";
            stm = con.prepareStatement(sql);
            stm.setInt(1, groupId);
            rs = stm.executeQuery();
            if (rs.next()) {
                Date d = new Date();
                d.setTime(Long.parseLong(rs.getString(1)));
                ret.setLastPostDate(d);
            }
        }

        if (files) {
            ArrayList<String> file = new ArrayList();
            ArrayList<Post> pstal = this.getAllPost(groupId);
            for (Post post : pstal) {
                file.addAll(this.getAllFileInPost(post.getPostid()));
            }
            ret.setAllFiles(file);
        }
        rs.close();
        stm.close();

        return ret;
    }

    public int getNumPartecipanti(int groupId) throws SQLException {
        int ret = 0;
        String sql = "SELECT count(*) FROM user_groups WHERE groupid=? AND status = 0";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupId);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            ret = rs.getInt(1);
        }
        rs.close();
        stm.close();
        return ret;
    }

    public int getNumPost(int groupId) throws SQLException {
        int ret = 0;
        String sql = "SELECT count(*) FROM post WHERE groupid=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupId);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            ret = rs.getInt(1);
        }
        rs.close();
        stm.close();
        return ret;
    }

    public boolean isClosedGroup(int groupId) throws SQLException {
        boolean ret = false;
        String sql = "SELECT private FROM groups WHERE groupid=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupId);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            ret = rs.getInt(1) > 1;
        }
        rs.close();
        stm.close();
        return ret;
    }

    public void closeGroup(int groupId) throws SQLException {
        String sql = "UPDATE groups SET private = (private+2) WHERE groupid=?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, groupId);
        stm.executeUpdate();
        stm.close();
    }

    public static int calculatePrivateColumnInt(boolean chiuso, boolean privato) {
        return (chiuso ? 2 : 0) + (privato ? 0 : 1);
    }

    //arr[0] = true se e' privato
    //arr[1] = true se e' chiuso
    public static boolean[] getPrivateAndClose(int i) {
        return new boolean[]{i == 0 || i == 2, i == 2 || i == 3};
    }

}
