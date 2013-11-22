/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import db.DBManager;
import html.Html;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author forna
 */
public class GroupHome extends HttpServlet {

    private DBManager dbm;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            connectToDatabase(req);
            HttpSession session = req.getSession();
            String username = (String) session.getAttribute(Login.SESSION_USER);
            generateHtml(req, resp, username);
        } catch (Exception e) {
        }
    }
    
    private void generateHtml(HttpServletRequest req, HttpServletResponse resp,String username) throws IOException{
        PrintWriter pw = resp.getWriter();
        pw.print(Html.addHtml("yoooohohohooh"));
    }
    
    private void connectToDatabase(HttpServletRequest req) {
        try {
            //cambiare qua cazzo
            dbm = new DBManager(req);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
