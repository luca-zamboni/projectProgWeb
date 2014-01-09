/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
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
import utils.Support;

/**
 *
 * @author jibbo
 */
public class Register extends HttpServlet {

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
        Support.forward(getServletContext(), request, response, "/register.jsp",null);
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
        String[] params = getParams(request);
        
        int code=0;
        boolean sanitized=checkInput(params,code);
        boolean isInserted=false;
        if(sanitized)
             isInserted= insertUser(request,params);
        Message msg = buildMessage(code,isInserted );
        if (msg.getType() == Message.MessageType.ERROR) {
            Support.forward(getServletContext(), request, response, "/register.jsp",msg);
        } else {
            Support.forward(getServletContext(), request, response, "/index.jsp",msg);
        }

    }

    public String[] getParams(HttpServletRequest request) {
        String[] out = new String[]{null, null, null};
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
            if (paramName.equals(RequestUtils.EMAIL)) {
                out[2] = paramValues[0];
            }
        }
        if (out[0] == null || out[0].length() < 1) {
            out[0] = out[2];
        }
        return out;
    }
    
    private boolean checkInput(String[] params,int code){
        if (!Support.isInputValid(params[0], 5)) {
            code=1;
            return false;
        }
        if (!Support.isInputValid(params[1], 5)) {
            code=2;
            return false;
        }
        if (!Support.isEmailValid(params[2])) {
            code=3;
            return false;
        }
        code=0;
        return true;
    }

    private Message buildMessage(int code ,boolean inserted) {
        
        //non e' riuscito perche' la mail/username gia' esiste
        if(!inserted)
            return new Message(Message.MessageType.ERROR,code);
        //everything is ok
        return new Message(Message.MessageType.SUCCESS, 0);
    }

    private boolean insertUser(HttpServletRequest request,String[] params) {
        try {
            DBManager dBManager = new DBManager(request);
            return dBManager.insertUser(params[0], params[1], params[2]);
        } catch (SQLException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
