/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        Support.forward(getServletContext(), request, response, "/register.jsp");
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
        Message msg = buildMessage(params);
        request.setAttribute(RequestUtils.MESSAGE, msg);
        Support.forward(getServletContext(), request, response, "/register.jsp");
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
        if(out[0]==null || out[0].length()<1)
            out[0]=out[2];
        return out;
    }

    private Message buildMessage(String[] params) {
        if(!Support.isInputValid(params[0], 5))
            return new Message(Message.MessageType.ERROR,1);
        if(!Support.isInputValid(params[1], 5))
           return new Message(Message.MessageType.ERROR,2);
        if(!Support.isEmailValid(params[2]))
           return new Message(Message.MessageType.ERROR,3);
        
        //everything is ok
        return new Message(Message.MessageType.SUCCESS,0);
    }
}
