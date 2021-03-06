/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Group;
import beans.Message;
import beans.UserBean;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.DBManager;
import utils.MailUtils;
import utils.Pair;
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
        ArrayList<UserBean> members = null;
        Message msg = null;
        int groupId = Integer.parseInt(request.
                getParameter(RequestUtils.GROUP_ID));
        int ownerId = -1;
        Group group = null;
        try {
            dbm = new DBManager(request);
            ub = dbm.getAllUser();
            group = dbm.fillGroupById(groupId, true, false, false);
            members = dbm.getAllUsersInGroup(groupId);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace(System.err);
        }
        
        ArrayList<Pair<UserBean,Boolean>> utenti = new ArrayList<>();
        for(UserBean u : ub){
            boolean check=false;
            for(UserBean m : members)
                if(m.getUserID()==u.getUserID()){
                    check=true;
                    break;
                }
            utenti.add(new Pair(u,check));
        }
        try {
            if(dbm.isClosedGroup(group.getGroupid())){
                Support.forward(getServletContext(), request, response,
                        request.getContextPath() + "/home", msg);
            }else{
                request.setAttribute(RequestUtils.GROUP, group);
                request.setAttribute(RequestUtils.GROUP_USERS, utenti);
                if (msg != null) {
                    Support.forward(getServletContext(), request, response,
                            request.getContextPath() + "/home", msg);
                } else {
                    Support.forward(getServletContext(), request, response, "/modgroup.jsp", msg);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GroupModify.class.getName()).log(Level.SEVERE, null, ex);
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

        String grpString = request.getParameter(RequestUtils.GROUP_ID);
        UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);
        final String title = request.getParameter(RequestUtils.GROUP_TITLE);
        String isPrivate = request.getParameter(RequestUtils.GROUP_PRIVATE);
        String[] usernames = request.getParameterValues(RequestUtils.GROUP_USERS);
        final int groupId = Integer.parseInt((grpString == null) ? "-1" : grpString);

        final List<Integer> users = new ArrayList<>();
        try {
            for (String username : usernames) {
                users.add(Integer.parseInt(username));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int rowChanged = updateGroup(groupId, title, isPrivate, null, users, user);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                 MailUtils.sendMail(users, dbm, groupId, title);
            }
        }).start();
       

        Message msg = buildMessage(groupId, title);

        if (msg.getType() == Message.MessageType.ERROR) {
            Support.forward(getServletContext(), request, response, "/modgroup?gid=" + groupId, msg);
        } else {
            Support.forward(getServletContext(), request, response, "/home", msg);
        }
    }

    private int updateGroup(int groupId, String title, String aPrivate, String chiuso, List<Integer> users, UserBean user) {
        int ret = 0;
        boolean check = title != null && !title.equals("");
        boolean prvt = aPrivate != null;
        if (check) {
            try {
                ret = dbm.updateGroup(groupId, title, users, user.getUserID(), chiuso != null, prvt);
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
                    + "modificare.");
        } else {
            msg = new Message(Message.MessageType.SUCCESS, -1, "Gruppo "
                    + " modificato con successo!");
        }
        return msg;
    }

}
