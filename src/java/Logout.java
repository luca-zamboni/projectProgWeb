/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * la classe pulisce il cookie del login e reindirizza alla pagina di login
 * @author jibbo
 */
public class Logout extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Logout</title>");            
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Logout as: " + 
                request.getSession().getAttribute(Login.SESSION_USER) +" "+ 
                request.getSession().getAttribute(Login.SESSION_DATA) 
                + "</h1>");
        out.println("</body>");
        out.println("</html>");
        clearSession(request);
        response.sendRedirect("./");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(Login.SESSION_USER);
        session.removeAttribute(Login.SESSION_DATA);
    }

}
