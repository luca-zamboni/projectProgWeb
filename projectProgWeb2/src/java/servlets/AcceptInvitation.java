/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
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
 * @author luca
 */
public class AcceptInvitation extends HttpServlet {

    private DBManager dbm;
    private UserBean user;
    private Message m;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            connectDatabase(req);
            HttpSession session = req.getSession();
            
            accInv(req, resp);

            Support.forward(getServletContext(), req, resp, "/home", m);
            
        } catch (Exception e) {

        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        connectDatabase(request);
        String[] credentials = getCredentials(request);
        String page = "/accinvmail.jsp";
        UserBean login = getUser(request, credentials[0], credentials[1]);
        Message msg = null;
        if (login.getUserID() >= 0) {
            Support.addToSession(request, SessionUtils.USER, login);
            try {
                System.err.println("asdiohashdojbasdòasyidgauisdgè9asoàugdp");
                accInv(request, response);
                
            } catch (SQLException ex) {
                Logger.getLogger(AcceptInvitation.class.getName()).log(Level.SEVERE, null, ex);
            }
            page = "/home";
        } else {
            msg = new Message(Message.MessageType.ERROR, 0);
            request.setAttribute(RequestUtils.MESSAGE, msg);
        }
        
        Support.forward(getServletContext(), request, response, page,msg);
        
    }
    
    public void accInv(HttpServletRequest req, HttpServletResponse response) throws SQLException{
        user = (UserBean) Support.getInSession(req, SessionUtils.USER);
        String group = req.getParameter("gid");
        String dec = req.getParameter("dec");
        int a = Integer.parseInt(group);
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
    }

    public String[] getCredentials(HttpServletRequest request) {
        String[] out = new String[]{null, null};
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramName.equals(RequestUtils.USERNAME)) {
                out[0] = paramValues[0];
            }
            if (paramName.equals(RequestUtils.PASSWD)) {
                out[1] = paramValues[0];
            }
        }
        return out;
    }

    /**
     *
     * @return null se errore server, -1 se non lo trova id dell'utente
     * altrimenti
     */
    private UserBean getUser(HttpServletRequest request, String username, String password) {
        try {
            UserBean out = dbm.login(username, password);
            out.setAvatar(dbm.getAvatar(out.getUserID()));
            return out;
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new UserBean(-1, 0, "", 0);
    }

    public void connectDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
            Support.putDBManager(request, dbm);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
