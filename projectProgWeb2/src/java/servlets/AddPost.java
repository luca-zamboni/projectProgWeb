/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.UserBean;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.lang.StringEscapeUtils;
import utils.DBManager;
import utils.ParsingUtils;
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class AddPost extends HttpServlet {
    
    private DBManager dbm;
    private int groupid;
    private UserBean user;
    private HttpServletRequest mReq;
    private HttpServletResponse mResp;
    private int error = 0;

   @Override 
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            mReq = req;
            mResp = resp;
            connectToDatabase();
            
            user = (UserBean) Support.getInSession(mReq, SessionUtils.USER);
            
            String group = req.getParameter(RequestUtils.GROUP_ID);
            groupid = Integer.parseInt(group);
            
            Message g = new Message(Message.MessageType.ERROR, 0, "You must write something");
            String post = getPostString();
            if (post == null || post.equals("")) {
                String referer = req.getHeader("Referer");
                    resp.sendRedirect(referer);
            } else {
                String aux = post.replace(" ", "");
                if (aux.equals("")) {
                    String referer = req.getHeader("Referer");
                    resp.sendRedirect(referer);
                } else {
                    /////////
                    post = cleanString(post);
                    post = post.replace("\n", "<br>");
                    
                    ParsingUtils p = new ParsingUtils(post,groupid,mReq);
                    post = p.parseQrAndLinkUtils();
                    
                    int postid = dbm.insertPost(user.getUserID(), groupid, post);
                    
                    p.generateQR(postid);
                    
                    saveFiles(postid);
                    if(error == 0) {
                        resp.sendRedirect("./threadgroup?gid=" + groupid);
                    }else{
                        Message m = new Message(Message.MessageType.ERROR, 0, "1 or More FIle are too big");
                        resp.sendRedirect("./threadgroup?gid=" + groupid);
                    }
                }
            }

            

        } catch (Exception e) {
            PrintWriter pw = resp.getWriter();
            pw.print("Error -- Something goes wrong -- Tips: Format your PC\n");
        }

    }

    private String cleanString(String post) {
        post = StringEscapeUtils.escapeHtml(post);
        return post;
    }

    private String getPostString() throws IOException, ServletException {
        String textPost = "";
        Collection<Part> p = mReq.getParts();
        for (Part part : p) {
            if (part.getName().equals("post")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
                StringBuilder value = new StringBuilder();
                char[] buffer = new char[1024];
                for (int length = 0; (length = reader.read(buffer)) > 0;) {
                    value.append(buffer, 0, length);
                }
                textPost = value.toString();
            }
        }
        return textPost;
    }
    
    private void saveFiles(int postid) throws IOException, ServletException, SQLException {
        Collection<Part> p = mReq.getParts();
        for (Part part : p) {
            if (part.getName().equals("post")) {
                //
            } else {
                if (part.getSize() >  1024 * 1024 * 10) {
                    error++;
                } else {
                    String path = mReq.getServletContext().getRealPath("/");
                    String name = part.getSubmittedFileName();
                    int i;
                    
                    if (isInGroupFiles(name)) {
                        for (i = 1; isInGroupFiles("(" + i + ")" + name); i++) ;
                        name = "(" + i + ")" + name;
                    }
                    
                    //Hotfixies for absoluth paths
                    
                    //IE
                    if(name.contains("\\")){
                        name = name.substring(name.lastIndexOf("\\"));
                    }
                    
                    //somthing else, never encountered
                    if(name.contains("/")){
                        name = name.substring(name.lastIndexOf("/"));
                    }
                    
                    if(part.getSize()>0)
                        dbm.newFile(user.getUserID(), postid , name);

                    File outputFile = new File(path + "/files/" + groupid + "/" + name);
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }
                    if (!outputFile.isDirectory()) {
                        InputStream finput = new BufferedInputStream(part.getInputStream());
                        OutputStream foutput = new BufferedOutputStream(new FileOutputStream(outputFile));

                        byte[] buffer = new byte[1024 * 500];
                        int bytes_letti = 0;
                        while ((bytes_letti = finput.read(buffer)) > 0) {
                            foutput.write(buffer, 0, bytes_letti);
                        }
                        finput.close();
                        foutput.close();
                    }
                }
            }

        }
        
    }
    
    public static ArrayList<String> getAllFileGroup(int groupid,HttpServletRequest mReq) {
        ArrayList<String> ret = new ArrayList();
        String path = mReq.getServletContext().getRealPath("/");
        File f = new File(path + "/files/" + groupid + "/");
        if (f.listFiles() != null) {
            for (final File fileEntry : f.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    ret.add(fileEntry.getName());
                }
            }
        }
        return ret;
    }
    
    private boolean isInGroupFiles(String file) {
        file = file.replace(" ", "");
        for (String f : getAllFileGroup(groupid,mReq)) {
            if (f.equals(file)) {
                return true;
            }
        }
        return false;
    }

    private void connectToDatabase() {
        try {
            dbm = new DBManager(mReq);
        } catch (SQLException ex) {
        }
    }
    
}
