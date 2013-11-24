/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db.DBManager;
import html.Html;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luca
 */
public class UploadAvatar extends HttpServlet {

    private DBManager dbm;
    private String dirName;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        connectToDatabase(request);
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute(Login.SESSION_USER);
        generateHtml(request, response, username);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        connectToDatabase(request);
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute(Login.SESSION_USER);
        PrintWriter pw = response.getWriter();
        pw.print(getFiles(request,response, user));
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // read the uploadDir from the servlet parameters
        dirName = config.getInitParameter("uploadDir");
        if (dirName == null) {
            throw new ServletException("Please supply uploadDir parameter");
        }

    }

    private void generateHtml(HttpServletRequest request, HttpServletResponse response, String user) throws IOException {
        try {
            String body = "", inform = "", avatar, submit = "Submit";
            PrintWriter pw = response.getWriter();

            body += Html.generateH(1, "Upload your AVATAR");

            avatar = dbm.getAvatar(dbm.getIdFromUser(user));
            if (avatar != null && (!avatar.equals(""))) {
                submit = "Change";
            } else {
                avatar = "img.jpg";
            }
            body += Html.getImageAvatar(avatar);
            
            inform += "<div class=\"form-group\">\n"
                    + "    <label for=\"avatar\">Avatar</label>\n"
                    + "    <input type=\"file\" name='avatar' id=\"avatar\">\n"
                    + "    <p class=\"help-block\">Hai mai visto Avatar?</p>\n"
                    + "  </div>\n"
                    + "<button type=\"submit\" class=\"btn btn-default\">Submit</button>\n";

            body += "<form enctype='multipart/form-data'\n"
                    + " method='POST' action='./uploadAvatar'>"
                    + inform
                    + "</form>";

            pw.print(Html.addHtml(body));
        } catch (Exception e) {
        }
    }

    private String getFiles(HttpServletRequest request,HttpServletResponse response, String user) throws IOException {
        String r = "";
        try {

            MultipartRequest multi = new MultipartRequest(request, dirName, 10 * 1024 * 1024,
                    "ISO-8859-1", new DefaultFileRenamePolicy());
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                String filename = multi.getFilesystemName(name);
                String type = multi.getContentType(name);
                
                String extension = "";

                int i = filename.lastIndexOf(".");
                if (i > 0) {
                    extension = filename.substring(i+1);
                }
                if(!extension.equals("png") && !extension.equals("jpg") &&  !extension.equals("jpeg")){
                    //response.sendRedirect("./uploadAvatar");
                }else{
                
                    dbm.setAvatar(user, extension);

                    File inputFile = multi.getFile(name);
                    String path = request.getServletContext().getRealPath("/");
                    File outputFile = new File(path+"/img/"+user+"."+extension);
                    if(!outputFile.exists()) {
                        outputFile.createNewFile();
                    } 

                    InputStream finput = new BufferedInputStream(new FileInputStream(inputFile));
                    OutputStream foutput = new BufferedOutputStream(new FileOutputStream(outputFile));

                    byte[] buffer = new byte[1024 * 500];
                    int bytes_letti = 0;
                    while ((bytes_letti = finput.read(buffer)) > 0) {
                        foutput.write(buffer, 0, bytes_letti);
                    }
                    finput.close();
                    foutput.close();
                    //response.sendRedirect("./uploadAvatar");
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(UploadAvatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect("./uploadAvatar");
        return r;
    }

    private void connectToDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
