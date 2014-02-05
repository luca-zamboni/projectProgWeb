/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.UserBean;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringEscapeUtils;
import utils.DBManager;
import utils.MailUtils;
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author forna
 */
@WebServlet(name = "GroupCreate", urlPatterns = {"/groupCreate"})
public class GroupCreate extends HttpServlet {

    private DBManager dbm;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<UserBean> ub = null;
        try {
            dbm = new DBManager(request);
            ub = dbm.getAllUser();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        request.setAttribute(RequestUtils.USERLIST, ub);
        Support.forward(getServletContext(), request, response, "/creategroup.jsp", null);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(GroupCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpSession session = request.getSession();
        UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);

        String title = request.getParameter(RequestUtils.GROUP_TITLE);
        String isPrivate = request.getParameter(RequestUtils.GROUP_PRIVATE);
        String[] usernames = request.getParameterValues(RequestUtils.USERCHECK);
        
        List<Integer> users = new ArrayList<>();
        try {
            for (String username : usernames) {
                users.add(dbm.getIdFromUser(username));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        title = StringEscapeUtils.escapeHtml(title);

        int groupId = addGroup(title, isPrivate, usernames, user);
        System.err.println(groupId);
        String path = request.getServletContext().getRealPath("/");
        File a = new File(path + "/files/" + groupId + "/");
        File b = new File(path + "/pdf/" + groupId + "/");
        a.mkdir();
        b.mkdir();
        
        

        MailUtils.sendMail(users, dbm, groupId, title);

        Message msg = buildMessage(groupId, title);

        if (msg.getType() == Message.MessageType.ERROR) {
            Support.forward(getServletContext(), request, response, "/creategroup.jsp", msg);
        } else {
            Support.forward(getServletContext(), request, response, "/home", msg);
        }
    }

    private int addGroup(String title, String aPrivate, String[] users, UserBean user) {
        int ret = -1;
        boolean check = title != null && !title.equals("");
        boolean prvt = aPrivate != null;
        if (check) {
            try {
                ret = dbm.newGroup(title, users, user.getUserID(), prvt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    private Message buildMessage(int groupId, String groupTitle) {
        Message msg;
        //qualcosa non e' andato a buon fine
        if (groupTitle == null || groupTitle.equals("")) {
            msg = new Message(Message.MessageType.ERROR, 10);
        } else if (groupId < 0) {
            msg = new Message(Message.MessageType.ERROR, 11);
        } else {
            msg = new Message(Message.MessageType.SUCCESS, 2);
        }
        return msg;
    }

}
