/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Group;
import beans.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class AdminModeratori extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            
            DBManager dbm = Support.getDBMangaer(req);
            UserBean user = (UserBean) Support.getInSession(req, SessionUtils.USER);

            ArrayList<Group> groups= dbm.getAllGroups(user.getUsername());
            
            for(Group p : groups){
                p.setNameOwner(dbm.getUserFormId(p.getOwner()));
            }

            req.setAttribute(RequestUtils.GROUPS, groups);
            
            Support.forward(getServletContext(), req, resp, "/adminmod.jsp", null);
        
        }catch(SQLException | ParseException e){
            
        }
        
    }

    
}
