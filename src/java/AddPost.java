/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import db.DBManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class AddPost extends HttpServlet {
    
    private DBManager dbm;
    private int groupid;
    private String username;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectToDatabase(req);
        HttpSession session = req.getSession();
        username = (String) session.getAttribute(Login.SESSION_USER);
        
        PrintWriter pw = resp.getWriter();
        String post = req.getParameter("post");
        if(post==null)
            resp.sendRedirect("./");
        
        String group = req.getParameter("g");
        if(group==null)
            resp.sendRedirect("./");
        
        groupid = Integer.parseInt(group);
        
        for(String t : getAllLinkedFile(req, post)){
            pw.print(t);
        }
        
    }
    
    private ArrayList<String> getAllLinkedFile(HttpServletRequest req, String parsethis){
        ArrayList<String> ret = new ArrayList();
        String[] a = parsethis.split("[$][$]");
        for(String h : a)
            if(isInGroupFiles(req, h))
                ret.add(h);
        return ret;
    }
    
    private boolean isInGroupFiles(HttpServletRequest req, String file){
        file = file.replace(" ", "");
        for(String f : GroupHome.getAllFileGroup(req,groupid)){
            if(f.equals(file))
                return true;
        }
        return false;
    }
    
    private void connectToDatabase(HttpServletRequest req) {
        try {
            dbm = new DBManager(req);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
