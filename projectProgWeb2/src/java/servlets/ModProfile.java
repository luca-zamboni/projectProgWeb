/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets; 

import beans.Message;
import beans.UserBean;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author jibbo
 */
public class ModProfile extends HttpServlet {

    private static String DEFAULT_EXT = "png";

    private String dirName;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // read the uploadDir from the servlet parameters
        dirName = config.getInitParameter("uploadDir");
        if (dirName == null) {
            throw new ServletException("Please supply uploadDir parameter");
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Support.forward(getServletContext(), request, response, "/profile.jsp", null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String param = request.getParameter(RequestUtils.PARAM);
        //Java quando gli passi un file in post sfancula tutto XD
        //ceercare di prendere gli altri parametrinn va(Guardato su internet)
        
        if (param != null && param.equals(RequestUtils.PASSWORDMOD)) {
            try {
                managePassword(request, request.getParameter(RequestUtils.PASSWD));
                Support.forward(getServletContext(), request, response, "/profile.jsp", new Message(Message.MessageType.SUCCESS,1));
            } catch (SQLException ex) {
                Logger.getLogger(ModProfile.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            manageAvatar(request, response);
        }
    }

    private void manageAvatar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File f = getUploadedFile(request);
        if (f != null) {
            if (isImage(f)) {
                try {
                    UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);
                    saveAvatar(request, response,user, f);
                } catch (Exception ex) {
                    Support.forward(getServletContext(), request, response, "/profile.jsp", new Message(Message.MessageType.ERROR, -1));

                }
                response.sendRedirect("./profile.jsp");
            } else {
                Support.forward(getServletContext(), request, response, "/profile.jsp", new Message(Message.MessageType.ERROR, 7));
            }
        } else {
            response.sendRedirect("./profile.jsp");
        }
    }

    private File getUploadedFile(HttpServletRequest request) throws IOException {
        MultipartRequest multi = new MultipartRequest(request, dirName,
                10 * 1024 * 1024, "ISO-8859-1", new FileRenamePolicy() {
                    @Override
                    public File rename(File file) {
                        return file;
                    }
                }
        );
        Enumeration files = multi.getFileNames();
        if (files.hasMoreElements()) {
            String name = (String) files.nextElement();
            return multi.getFile(name);
        }
        throw new FileNotFoundException();
    }

    private boolean isImage(File f) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        mimeTypesMap.addMimeTypes("image png bmp jpeg jpg");
        String mime = mimeTypesMap.getContentType(f);
        return mime.contains("image");
    }

    private void saveAvatar(HttpServletRequest request,
            HttpServletResponse response, UserBean user, File f) throws IOException {
        try {
            DBManager dbm = new DBManager(request);
            String path = request.getServletContext().getRealPath("/");
            String fullpath= path+"img/" + user.getUserID() + "." + DEFAULT_EXT;
            
            dbm.setAvatar(user.getUserID(),fullpath);
            
            File outputFile = new File(path + "img/" + user.getUserID() + "." + DEFAULT_EXT);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            InputStream finput = new BufferedInputStream(new FileInputStream(f));
            OutputStream foutput = new BufferedOutputStream(
                    new FileOutputStream(outputFile));

            //Resizing
            Image img = ImageIO.read(finput);
            int x = img.getWidth(null);
            int y = img.getHeight(null);
            float k = ((float) x) / ((float) y);
            if (k <= 1) {
                y = 200;
                x = (int) (((float) y) * k);
            } else {
                x = 200;
                y = (int) (((float) x) / k);
            }

            BufferedImage resizedImage = new BufferedImage(x, y, BufferedImage.TYPE_USHORT_565_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(img, 0, 0, x, y, null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);

            ImageIO.write(resizedImage, DEFAULT_EXT, foutput);
            finput.close();
            foutput.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(ModProfile.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void managePassword(HttpServletRequest request, String pass) throws SQLException {
        DBManager dbm = new DBManager(request);
        UserBean bean = (UserBean) Support.getInSession(request, SessionUtils.USER);
        dbm.setPassword(bean.getUserID(), pass);
    }
}
