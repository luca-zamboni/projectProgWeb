/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import static java.nio.file.StandardCopyOption.*;
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
import java.nio.file.Files;
import java.nio.file.Path;
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
        pw.print(getFiles(request, user));
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
            pw.println("<html>");
            pw.print(Html.includeHead());
            pw.print("<body>");

            body += Html.generateH(1, "Upload your AVATAR");

            avatar = dbm.getAvatar(dbm.getIdFromUser(user));
            if (avatar != null && (!avatar.equals(""))) {
                submit = "Change";
                body += "<img src=\"img/" + avatar + "\" style='width:150px;heigth:200px;' alt=\"This is you? You are so ugly...\" class=\"img-thumbnail\">";
            } else {
                body += "<img src=\"img/img.jpg\"style='width:150px;heigth:200px;' alt=\"Why? tell me why?\" class=\"img-thumbnail\">";
            }

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

            pw.print(Html.centerInPage(body));
            pw.println("</body>");
            pw.println("</html>");
        } catch (Exception e) {
        }
    }

    private String getFiles(HttpServletRequest request, String user) {
        String r = "";
        try {

            MultipartRequest multi = new MultipartRequest(request, dirName, 10 * 1024 * 1024,
                    "ISO-8859-1", new DefaultFileRenamePolicy());
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                String filename = multi.getFilesystemName(name);
                String type = multi.getContentType(name);
                
                File inputFile = multi.getFile(name);
                //"/home/luca/projects/JavaServlet/oneProject/web";
                String path = request.getServletContext().getRealPath("/");
                System.out.println(path);
                File outputFile = new File(path+"/img/"+user+".png");
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

            }

        } catch (Exception ex) {
            Logger.getLogger(UploadAvatar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    private void connectToDatabase(HttpServletRequest request) {
        try {
            //cambiare qua cazzo
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
