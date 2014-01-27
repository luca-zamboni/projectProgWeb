/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.DBManager;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class AcceptInvitation extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            
            DBManager dbm = Support.getDBMangaer(req);
            HttpSession session = req.getSession();
            UserBean user = (UserBean) Support.getInSession(req, SessionUtils.USER);
            
            String group = req.getParameter("gid");
            String dec = req.getParameter("dec");
            int a = Integer.parseInt(group);
            Message m;
            if (group != null && dbm.isPending(user.getUserID(),a) ) {
                if(dec != null && dec.equals("1")){
                    dbm.declinePending(a,user.getUserID());
                    m = new Message(Message.MessageType.SUCCESS, 3);
                }else{
                    m = new Message(Message.MessageType.SUCCESS, 2);
                    dbm.acceptPending(a, dbm.getIdFromUser(user.getUsername()));
                }
            } else {
                m = new Message(Message.MessageType.ERROR, 999);
            }
            
            Support.forward(getServletContext(), req, resp, "/home", m);
            
        } catch (Exception e) {

        }
    }
   
}
