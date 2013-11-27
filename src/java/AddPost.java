/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db.DBManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author luca
 */
public class AddPost extends HttpServlet {

    private DBManager dbm;
    private int groupid;
    private String user;
    private HttpServletRequest mReq;
    private HttpServletResponse mResp;
    private String dirName;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            mReq = req;
            mResp = resp;
            connectToDatabase();
            HttpSession session = req.getSession();
            user = (String) session.getAttribute(Login.SESSION_USER);
            String group = req.getParameter("g");
            groupid = Integer.parseInt(group);

            PrintWriter pw = mResp.getWriter();
            String post = getPostAndFiles();
            if (post == null || post.equals("")) {
                String referer = req.getHeader("Referer");
                resp.sendRedirect(referer);
            } else {
                String aux = post.replace(" ", "");
                if (aux.equals("")) {
                    String referer = req.getHeader("Referer");
                    resp.sendRedirect(referer);
                } else {
                    
                    post = cleanString(post);
                    post = getStringWithLink(post);
                    dbm.insertPost(dbm.getIdFromUser(user), groupid, post);

                    pw.print(post);
                }
            }

            // queste due istruzioni rimandano alla pagina precedente
            String referer = req.getHeader("Referer");
            resp.sendRedirect(referer);

        } catch (IOException | NumberFormatException | SQLException | ServletException e) {
            PrintWriter pw = resp.getWriter();
            pw.print("Error -- Something goes wrong -- Tips: Format your PC\n");
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }

    }
    
    private String cleanString(String post){
        post = post.replaceAll("<", "&lt;");
        post = post.replaceAll(">", "&gt;");
        return post;
    }
    
    private String getPostAndFiles() throws IOException, ServletException{
        String textPost = "";
        Collection<Part> p = mReq.getParts();
        for(Part part : p){
            if(part.getName().equals("post")){
                BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
                StringBuilder value = new StringBuilder();
                char[] buffer = new char[1024];
                for (int length = 0; (length = reader.read(buffer)) > 0;) {
                    value.append(buffer, 0, length);
                }
                textPost = value.toString();
            }else{
                String path = mReq.getServletContext().getRealPath("/");
                File outputFile = new File(path + "/files/" +groupid +"/"+ part.getSubmittedFileName());
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                if(!outputFile.isDirectory()){
                InputStream finput = new BufferedInputStream(part.getInputStream());
                OutputStream foutput = new BufferedOutputStream(new FileOutputStream(outputFile));

                byte[] buffer = new byte[1024 * 500];
                int bytes_letti = 0;
                while ((bytes_letti = finput.read(buffer)) > 0) {
                    foutput.write(buffer, 0, bytes_letti);
                }
                finput.close();
                foutput.close();}
            }
            
        }
        return textPost;
    }
    
    private String getStringWithLink(String post){
        String ret = "";
        String path = mReq.getServletContext().getRealPath("/");
        path+= "/files/"+groupid+"/";
        String[] a = post.split("[$][$]");
        for (String h : a) {
            if (isInGroupFiles(h)) {
                ret+="<a href='./files/"+groupid+"/"+h+"'>"+h+"</a>";
            }else{
                String[] split = h.split("\\s");
                String ris = "";
                for(String s : split){
                    try{
                        URL url = new URL(s);
                        ris += "<a href='"+url+"'>" + url +"</a>";
                    }catch(MalformedURLException e){
                        ris += s+" ";
                    }
                }
                ret+=ris;
            }
        }
        return ret;
    }

    private ArrayList<String> getAllLinkedFile(String parsethis) {
        ArrayList<String> ret = new ArrayList();
        String[] a = parsethis.split("[$][$]");
        for (String h : a) {
            if (isInGroupFiles(h)) {
                ret.add(h);
            }
        }
        return ret;
    }

    private boolean isInGroupFiles(String file) {
        file = file.replace(" ", "");
        for (String f : GroupHome.getAllFileGroup(mReq, groupid)) {
            if (f.equals(file)) {
                return true;
            }
        }
        return false;
    }
    
    /*public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // read the uploadDir from the servlet parameters
        dirName = config.getInitParameter("uploadDir");
        if (dirName == null) {
            throw new ServletException("Please supply uploadDir parameter");
        }

    }*/

    private void connectToDatabase() {
        try {
            dbm = new DBManager(mReq);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
