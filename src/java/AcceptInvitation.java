/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import db.DBManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luca
 */
public class AcceptInvitation extends HttpServlet {
    
    private DBManager dbm;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            connectToDatabase();
            HttpSession session = req.getSession();
            String username = (String) session.getAttribute(Login.SESSION_USER);
            
            String group = req.getParameter("g");
            String dec = req.getParameter("dec");
            int a = Integer.parseInt(group);
            if (group != null && dbm.isPending(dbm.getIdFromUser(username),a) ) {
                if(dec != null && dec.equals("1")){
                    dbm.declinePending(a, dbm.getIdFromUser(username));
                    resp.sendRedirect("./home?acc=2&g="+a);
                }else{
                    dbm.acceptPending(a, dbm.getIdFromUser(username));
                    resp.sendRedirect("./home?acc=1&g="+a);
                }
            } else {
                resp.sendRedirect("./home");
            }
        } catch (Exception e) {

        }
    }
    
    private void connectToDatabase() {
        try {
            //cambiare qua cazzo
            dbm = new DBManager();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
