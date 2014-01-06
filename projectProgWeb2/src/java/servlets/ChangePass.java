/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.RequestUtils;

/**
 *
 * @author luca
 */
public class ChangePass extends HttpServlet {
    
    DBManager dbm;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        connectDatabase(req);
        
        String pass = (String) req.getParameter(RequestUtils.PASSWD);
        String code = (String) req.getParameter(RequestUtils.CODE);
        
        long scadenza = dbm.getScadenza(code);
        long now = new Date().getTime();
        
        if(scadenza > now){
            req.getSession().setAttribute(RequestUtils.MESSAGE, new beans.Message(beans.Message.MessageType.ERROR, 4));
            resp.sendRedirect("./changepassword.jsp");
        }else{
            if(!dbm.setNewPassword(code,pass)){
                req.getSession().setAttribute(RequestUtils.MESSAGE, new beans.Message(beans.Message.MessageType.ERROR, 0));
                resp.sendRedirect("./changepassword.jsp");
            }
            resp.sendRedirect("./");
        }
        
    }

    
    public void connectDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
