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
 * Classe usata per gestire la risposta inviata dall'utente su un invito
 * a un gruppo (accetta/rifiuta)
 * @author luca
 */
public class AcceptInvitation extends HttpServlet {
    
    private DBManager dbm;
    
    /**
     * in base ai parametri g che indica il groupid e dec che indica se
     * e' stato rifiutato modifica sul db le informazioni sull'utente 
     * relative al gruppo per poi reindirizzare alla home
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            connectToDatabase(req);
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
    
    private void connectToDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
