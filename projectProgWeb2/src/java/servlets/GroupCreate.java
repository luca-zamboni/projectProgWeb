/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
@WebServlet(name = "GroupCreate", urlPatterns = {"/groupCreate"})
public class GroupCreate extends HttpServlet {

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
            DBManager dbm = new DBManager(request);
            ub = dbm.getAllUser();
        } catch (Exception e) {
            System.out.println(e.toString());
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

        String title = request.getParameter(RequestUtils.GROUP_TITLE);
        String isPrivate = request.getParameter(RequestUtils.GROUP_PRIVATE);
        String[] users = request.getParameterValues(RequestUtils.GROUP_USERS);

        int groupId = addGroup(request, title, isPrivate, users);

        Message msg = buildMessage(groupId, title);
        
        if (msg.getType() == Message.MessageType.ERROR) {
            Support.forward(getServletContext(), request, response, "/creategroup.jsp",msg);
        } else {
            Support.forward(getServletContext(), request, response, "/home.jsp",msg);
        }
    }

    private int addGroup(HttpServletRequest request, String title, String aPrivate, String[] users) {
        int ret = -1;
        boolean check = title!=null && !title.equals("");
        boolean prvt = aPrivate!=null;
        if (check) {
            try {
                DBManager dbm = new DBManager(request);
                HttpSession session = request.getSession();
                UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER); //TODO fix nullpointerexception on user here!
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
        if (groupTitle==null || groupTitle.equals("")) {
            msg = new Message(Message.MessageType.ERROR, 11, "Impossibile "
                    + "inserire un gruppo senza nome nel database");
        } else if (groupId<0){
            msg = new Message(Message.MessageType.ERROR, 10, "Impossibile "
                    + "inserire "+groupTitle+" nel database");
        } else {
            msg = new Message(Message.MessageType.SUCCESS, 0, "Gruppo "
                    +groupTitle+" inserito con successo!");
        }
        return msg;
    }

}
