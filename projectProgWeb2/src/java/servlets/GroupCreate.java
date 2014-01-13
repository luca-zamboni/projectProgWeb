/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.DBManager;
import utils.RequestUtils;
import utils.Support;

/**
 *
 * @author forna
 */
@WebServlet(name = "GroupManage", urlPatterns = {"/GroupManage"})
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
        Support.forward(getServletContext(), request, response, "/creategroup.jsp",null);
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
        
    }

    private int addGroup(HttpServletRequest request, String title, String aPrivate, String[] users) {
        int ret = -1;
        
        //TODO need a way to get userId
//        try {
//            DBManager dbm = new DBManager(request);
//            HttpSession session = request.getSession();
//            session.getAttribute()
//            dbm.newGroup(title, users, , aPrivate.equals("true"));
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        
//        return ret;
    }
    
}
