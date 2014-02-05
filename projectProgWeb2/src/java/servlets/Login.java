/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.UserBean;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author jibbo
 */
public class Login extends HttpServlet {

    private DBManager dbm;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        connectDatabase(request);
        Support.forward(getServletContext(), request, response, "/index.jsp", new Message(Message.MessageType.ERROR, 0));
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
        connectDatabase(request);
        String[] credentials = getCredentials(request);
        String page = "/index.jsp";
        UserBean login = getUser(request, credentials[0], credentials[1]);
        Message msg = null;
        if (login.getUserID() >= 0) {
            Support.addToSession(request, SessionUtils.USER, login);
            page = "/home";
        } else {
            msg = new Message(Message.MessageType.ERROR, 0);
            request.setAttribute(RequestUtils.MESSAGE, msg);
        }
        Support.forward(getServletContext(), request, response, page,msg);
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
            Support.putDBMangaer(request, dbm);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
