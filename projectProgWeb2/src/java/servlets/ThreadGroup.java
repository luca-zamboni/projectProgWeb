/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Group;
import beans.Post;
import beans.UserBean;
import static beans.UserBean.DEFAULT_IMG_PATH;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import utils.DBManager;
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class ThreadGroup extends HttpServlet {
    
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private DBManager dbm;
    private int groupid;
    private Group group;

    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            
            connectToDatabase(req);
            UserBean user = (UserBean) Support.getInSession(req, SessionUtils.USER);
            
            this.req = req;
            this.resp = resp;
            groupid = Integer.parseInt((String) req.getParameter(RequestUtils.GROUP_ID));
            
            String title = dbm.getGroupTitleById(groupid);
            if(user == null){
                System.err.println("sdasd");
                title = replaceEmail(title);
            }
            int owner = dbm.getGroupOwnerById(groupid);
            ArrayList<Post> posts = dbm.getAllPost(groupid);
            for(Post a: posts){
                
                if(user == null){
                    a.setText(replaceEmail(a.getText()));
                    a.setUser(replaceEmail(a.getUser()));
                }
                
                String path = req.getServletContext().getRealPath("/");
                File f = new File(path + "/imgs_profiles/" + a.getUserid() + ".png");
                if (f.exists()) {
                    a.setAvatar(ModProfile.IMG_PROF_DIR + a.getUserid() + ".png");
                }else{
                    a.setAvatar(DEFAULT_IMG_PATH + "img.jpg" );
                }
                
                
                
            }
            
            boolean isPrivate = dbm.isPrivateGroup(groupid);
            
            group = new Group(groupid, title, owner, isPrivate);
            group.setChiuso(dbm.isClosedGroup(groupid));
            group.setPosts(posts);
            if(isPrivate){
                group.setUsers(dbm.getAllUserInGroup(groupid));
            }
            
            for(Post aux :posts){
                aux.setFiles(dbm.getAllFileInPost(aux.getPostid()));
                String path = req.getServletContext().getRealPath("/");
                File f = new File(path + "/files/" + groupid + "/qr/" + aux.getPostid());
                if (f.listFiles() != null) {
                    for (final File fileEntry : f.listFiles()) {
                        if (!fileEntry.isDirectory()) {
                            aux.setQrs("./files/" + groupid + "/qr/" + aux.getPostid()+"/" + fileEntry.getName());
                        }
                    }
                }
                group.setAllFiles(aux.getFiles());
            }
            
            ServletContext ctx = req.getServletContext();
            ctx.setAttribute("group", group);
            
            Support.forward(ctx, req, resp, "/groupthread.jsp", null);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ThreadGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private String replaceEmail(String text){
        if(isValidEmailAddress(text)){
            return text.substring(0,text.indexOf("@")+1)+"xxxXXxxx" ;
        }
        return text;
    }
    
    public boolean isValidEmailAddress(String email) {
       java.util.regex.Pattern p = java.util.regex.Pattern.compile(".+@.+\\.[a-z]+");
       java.util.regex.Matcher m = p.matcher(email);
       return m.matches();
    }
    
    private void connectToDatabase(HttpServletRequest req) {
        try {
            dbm = Support.getDBManager(req);
            if(dbm==null)
                dbm = new DBManager(req);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
