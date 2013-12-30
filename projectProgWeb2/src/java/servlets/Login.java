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
        request.setAttribute(RequestUtils.MESSAGE, new Message("parametri non validi"));
        Support.forward(getServletContext(), request, response, "/index.jsp");
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
        String[] credentials = getCredentials(request);
        String page = "/index.jsp";
        if(loginValid(request,credentials[0],credentials[1])){
            UserBean user = new UserBean(credentials[0]);
            Support.addToSession(request, SessionUtils.USER, user);
            page="home.jsp";
        }else{
            Message msg = new Message("Parametri non validi");
            request.setAttribute(RequestUtils.MESSAGE, msg);
        }
        Support.forward(getServletContext(), request, response, page);
    }
    
    public String[] getCredentials(HttpServletRequest request){
        String[] out = new String[]{null,null};
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
    
    private boolean loginValid(HttpServletRequest request,String username,String password) {
        try {
            DBManager dbm = new DBManager(request);
            return dbm.login(username, password);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

}
