/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Group;
import beans.Post;
import beans.UserBean;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.RequestUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class threadgroup extends HttpServlet {
    
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private DBManager dbm;
    private int groupid;
    private Group group;

    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            
            connectToDatabase(req);
            this.req = req;
            this.resp = resp;
            groupid = Integer.parseInt((String) req.getParameter(RequestUtils.GROUP_ID));
            
            String title = dbm.getGroupTitleById(groupid);
            int owner = dbm.getGroupOwnerById(groupid);
            ArrayList<Post> posts = dbm.getAllPost(groupid);
            
            boolean isPrivate = dbm.isPrivateGroup(groupid);
            
            group = new Group(groupid, title, owner, isPrivate);
            group.setPosts(posts);
            if(isPrivate){
                group.setUsers(dbm.getAllUserInGroup(groupid));
            }
            
            for(Post aux :posts){
                aux.setFiles(dbm.getAllFileInPost(aux.getPostid()));
                group.setAllFiles(aux.getFiles());
            }
            
            ServletContext ctx = req.getServletContext();
            ctx.setAttribute("group", group);
            
            Support.forward(ctx, req, resp, "/groupthread.jsp", null);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(threadgroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private void connectToDatabase(HttpServletRequest req) {
        try {
            dbm = new DBManager(req);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
