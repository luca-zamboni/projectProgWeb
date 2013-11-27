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
import models.Post;

/**
 *
 * @author forna
 */
public class GroupHome extends HttpServlet {

    private DBManager dbm;
    private int groupid;
    private String username;
    private HttpServletRequest mReq;
    private HttpServletResponse mResp;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            mReq = req;
            mResp = resp;
            connectToDatabase(req);
            HttpSession session = req.getSession();
            username = (String) session.getAttribute(Login.SESSION_USER);
            groupid = Integer.parseInt(req.getParameter("g"));
            
            generateHtml();
        } catch (Exception e) {
        }
    }
    
    private void generateHtml() throws IOException, SQLException{
        PrintWriter pw = mResp.getWriter();
        String body = "";
        body += "<style>h2{text-align:center}</style>";
        body += Html.generateH(2, dbm.getGroupTitleById(groupid)) + "<br>";
        body += generateThread(username);
        body += "<form method='POST' action='addPost?g="+groupid+"' enctype='multipart/form-data'>"
                    + generateNewPostForm()
                    + "</form>";
        
        pw.print(Html.addHtml(body,username));
        
    }
    
    private String generateNewPostForm(){
        String form = "";
        
        form += "<div class=\"form-group\">\n" +
                "<label for=\"exampleInputFile\">File input</label>\n" +
                "<input type=\"file\" name=\"files\" multiple/>\n" +
                "<p class=\"help-block\">Insert your files</p>\n" +
                "</div>\n";
        form+="<textarea name=\"post\" class=\"form-control\" rows=\"6\"></textarea><br>";
        form+=Html.generateButtonSubmit("Submit", "btn btn-success btn-lg pull-right");
        
        return form;
    }
    
    private String generateThread(String username) throws SQLException{
        String thread = "";
        ArrayList<Post> p = dbm.getAllPost(groupid);
        for(Post h : p){
            thread += "<div class=\"well\">\n";
            thread += "<div style=\"font-size:16px;\"><b>" + dbm.getUserFormId(h.getOwner())
                    + "</b> <span style=\"font-size:12px;\">says:</span>"
                    + "</div> <br>\n";
            thread += h.getText();
            thread += "<br><br><div style=\"font-size:12px;\">Posted on "+ h.getCreationdate() + "</div>";
            thread += "</div>";
        }
        return thread;
    }
    
    public static ArrayList<String> getAllFileGroup(HttpServletRequest req,int groupid){
        ArrayList<String> ret = new ArrayList();
        String path = req.getServletContext().getRealPath("/");
        File f = new File(path+"/files/"+groupid+"/");
        if(f.listFiles() != null){
            for (final File fileEntry : f.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    ret.add(fileEntry.getName());
                }
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
