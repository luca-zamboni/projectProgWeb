/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import db.DBManager;
import html.Html;
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
 * @author forna
 */
public class GroupHome extends HttpServlet {

    private DBManager dbm;
    private int groupid;
    private String username;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            connectToDatabase(req);
            HttpSession session = req.getSession();
            username = (String) session.getAttribute(Login.SESSION_USER);
            String group = req.getParameter("g");
            if(group==null)
                resp.sendRedirect("./");
            groupid = Integer.parseInt(group);
            
            generateHtml(req, resp);
        } catch (Exception e) {
        }
    }
    
    private void generateHtml(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter pw = resp.getWriter();
        String body = "";
        
        body += generateThread(req,resp,username);
        
        String group = req.getParameter("g");
        if(group!=null)
            body += Html.generateForm("addPost?g="+group, "POST", generateNewPostForm());
        
        pw.print(Html.addHtml(body));
        
    }
    
    private String generateNewPostForm(){
        String form = "";
        
        form+="<textarea name=\"post\" class=\"form-control\" rows=\"6\"></textarea><br>";
        form+=Html.generateButtonSubmit("Submit", "btn btn-success");
        
        return form;
    }
    
    private String generateThread(HttpServletRequest req, HttpServletResponse resp,String username){
        ////// request all post :)
        return "All post !!!";
    }
    
    public static ArrayList<String> getAllFileGroup(HttpServletRequest req,int groupid){
        ArrayList<String> ret = new ArrayList();
        String path = req.getServletContext().getRealPath("/");
        File f = new File(path+"/files/"+groupid+"/");
        for (final File fileEntry : f.listFiles()) {
            if (!fileEntry.isDirectory()) {
                ret.add(fileEntry.getName());
            }
        }
        return ret;
    }
    
    private void connectToDatabase(HttpServletRequest req) {
        try {
            dbm = new DBManager(req);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
