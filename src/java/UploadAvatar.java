/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import db.DBManager;
import html.HtmlHelper;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
import javax.imageio.ImageIO;
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
        pw.print(getFiles(request, response, user));
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

            body += HtmlHelper.generateH(1, "Upload your AVATAR");

            avatar = dbm.getAvatar(dbm.getIdFromUser(user));
            if (avatar != null && (!avatar.equals(""))) {
                submit = "Change";
            } else {
                avatar = "img.jpg";
            }
            body += HtmlHelper.getImageAvatar(avatar);

            String erru = getErrString(request);

            inform += "<div class=\"form-group\">\n"
                    + "    <label for=\"avatar\">Avatar</label>\n"
                    + "    <input type=\"file\" name='avatar' id=\"avatar\">\n"
                    + (erru.equals("") ? "    <p class=\"help-block\">Scegli una immagine dal tuo pc!</p>\n" : erru)
                    + "  </div>\n"
                    + "<button type=\"submit\" class=\"btn btn-default\">Submit</button>\n";

            body += "<form enctype='multipart/form-data'\n"
                    + " method='POST' action='./uploadAvatar'>"
                    + inform
                    + "</form>";

            pw.print(HtmlHelper.addHtml(body, user,dbm.getAvatar(dbm.getIdFromUser(user))));
        } catch (Exception e) {
        }
    }

    private String getErrString(HttpServletRequest req) {
        String str = "";
        String err = req.getParameter("err");
        int e;

        if (err != null) {
            e = Integer.parseInt(err);
            switch (e) {
                case 1:
                    str = "This is not an image!";
                    break;
                case 2:
                    str = "File too big!";
                    break;
                default:
                    str = "";
                    break;
            }
            if (str.equals("")) {
                return str;
            }
            str = HtmlHelper.generateHWithColor(4, str, "text-danger");
        }

        return str;
    }

    private String getFiles(HttpServletRequest request, HttpServletResponse response, String user) throws IOException {
        String r = "";
        try {

            MultipartRequest multi = new MultipartRequest(request, dirName, 10 * 1024 * 1024,
                    "ISO-8859-1", new FileRenamePolicy() {

                @Override
                public File rename(File file) {
                    return file;
                }
            });
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                String filename = multi.getFilesystemName(name);
                String type = multi.getContentType(name);
                File inputFile = multi.getFile(name);

                System.err.println(type + "\t" + inputFile.length());

                String extension = "";

                int i = filename.lastIndexOf(".");
                if (i > 0) {
                    extension = filename.substring(i + 1);
                }
                if (!type.contains("image")) {
                    response.sendRedirect("./uploadAvatar?err=1");
                } else if (inputFile.length() > 3 * 1024 * 1024) {
                    response.sendRedirect("./uploadAvatar?err=2");
                } else {

                    //inputFile
                    dbm.setAvatar(user, "png");

                    String path = request.getServletContext().getRealPath("/");
                    File outputFile = new File(path + "/img/" + user + ".png");
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }

                    InputStream finput = new BufferedInputStream(new FileInputStream(inputFile));
                    OutputStream foutput = new BufferedOutputStream(new FileOutputStream(outputFile));
                    
                    Image img = ImageIO.read(finput);
                    int x = img.getWidth(null);
                    int y = img.getHeight(null);
                    float k = ((float) x)/((float) y);
                    if (k<=1) {
                        y=200;
                        x=(int) (((float) y)*k);
                    } else {
                        x=200;
                        y=(int) (((float) x)/k);
                    }

                    BufferedImage resizedImage = new BufferedImage(x, y, BufferedImage.TYPE_USHORT_565_RGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.drawImage(img, 0, 0, x, y, null);
                    g.dispose();
                    g.setComposite(AlphaComposite.Src);
                    
                    ImageIO.write(resizedImage, "png", foutput);
                    finput.close();
                    foutput.close();
                    response.sendRedirect("./uploadAvatar");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(UploadAvatar.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("./uploadAvatar");
        }
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
