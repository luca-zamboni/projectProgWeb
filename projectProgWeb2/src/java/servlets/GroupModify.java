/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Group;
import beans.Message;
import beans.UserBean;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.DBManager;
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author forna
 */
public class GroupModify extends HttpServlet {

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
        Group grp = null;
        Message msg = null;
        try {
            dbm = new DBManager(request);
            ub = dbm.getAllUser();
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        request.setAttribute(RequestUtils.USERLIST, ub);
        System.err.println(request.getAttribute(RequestUtils.GROUP_OWNER));
        if (msg != null) {
            Support.forward(getServletContext(), request, response, 
                    "/home.jsp", msg);
        } else {
            Support.forward(getServletContext(), request, response, "/modifygroup.jsp", msg);
        }
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
            Logger.getLogger(GroupModify.class.getName()).log(Level.SEVERE, null, ex);
        }

        HttpSession session = request.getSession();

        String grpString = request.getParameter(RequestUtils.GROUP_OWNER);
        UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);
        String title = request.getParameter(RequestUtils.GROUP_TITLE);
        String isPrivate = request.getParameter(RequestUtils.GROUP_PRIVATE);
        String[] users = request.getParameterValues(RequestUtils.GROUP_USERS);
        int groupId =  Integer.parseInt( (grpString==null) ? "-1" : grpString);

        int rowChanged = updateGroup(groupId, title, isPrivate, users, user);

        Message msg = buildMessage(groupId, title);

        if (msg.getType() == Message.MessageType.ERROR) {
            Support.forward(getServletContext(), request, response, "/modifygroup.jsp", msg);
        } else {
            Support.forward(getServletContext(), request, response, "/home.jsp", msg);
        }
    }

    private int updateGroup(int groupId, String title, String aPrivate, String[] users, UserBean user) {
        int ret = 0;
        boolean check = title != null && !title.equals("");
        boolean prvt = aPrivate != null;
        if (check) {
            try {
                ret = dbm.updateGroup(groupId, title, users, user.getUserID(), prvt);
            } catch (Exception e) {
                e.printStackTrace();
                ret = 0;
            }
        }

        return ret;
    }

    private Message buildMessage(int groupId, String groupTitle) {
        Message msg;
        //qualcosa non e' andato a buon fine
        if (groupTitle == null || groupTitle.equals("")) {
            msg = new Message(Message.MessageType.ERROR, 21, "Impossibile "
                    + "inserire un nome vuoto nel database.");
        } else if (groupId == 0) {
            msg = new Message(Message.MessageType.ERROR, 22, "Impossibile "
                    + "modificare " + groupTitle + ".");
        } else if (groupId > 1) {
            msg = new Message(Message.MessageType.ERROR, 23, "Incredibile! "
                    + "hai addirittura modificato 2 gruppi");
        } else {
            msg = new Message(Message.MessageType.SUCCESS, 3, "Gruppo "
                    + " modificato con successo!");
        }
        return msg;
    }

}
