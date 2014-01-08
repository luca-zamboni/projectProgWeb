/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import db.DBManager;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Classe che gestisce l'aggiunta di un post al gruppo
 * @author luca
 */
public class AddPost extends HttpServlet {

    private DBManager dbm;
    private int groupid;
    private String user;
    private HttpServletRequest mReq;
    private HttpServletResponse mResp;
    private int error = 0;

    /**
     * Usiamo una post per gestire l'invio di messaggi in un gruppo da
     * parte dell'utente, inseriamo il messaggio nel database quindi 
     * reindirizziamo alla home del gruppo per il refresh
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
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
                    getFiles();
                    post = cleanString(post);
                    post = post.replace("\n", "<br>");
                    post = getStringWithLink(post);
                    dbm.insertPost(dbm.getIdFromUser(user), groupid, post);

                    pw.print(post);
                }
            }

            if(error == 0) 
                resp.sendRedirect("./groupHome?g=" + groupid);
            else
                resp.sendRedirect("./groupHome?g=" + groupid +"&err=" + error);

        } catch (IOException | NumberFormatException | SQLException | ServletException e) {
            PrintWriter pw = resp.getWriter();
            pw.print("Error -- Something goes wrong -- Tips: Format your PC\n");
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * rimuore i tag < e > di html per evitare che possano essere 
     * lanciati script direttamente da commento sul server
     * @param post
     * @return 
     */
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

    /**
     * metodo usato per gestire il caso di upload di file multipli da 
     * parte del'utente su un singolo post, siccome la libreria 
     * utilizzata non gestiva questo particolare caso.
     * @throws IOException
     * @throws ServletException
     * @throws SQLException 
     */
    private void getFiles() throws IOException, ServletException, SQLException {
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
                    
                    dbm.newFile(dbm.getIdFromUser(user), groupid, name);

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

    /**
     * metodo che si occupa di fare il parse dei link tra $$link$$
     * @param post
     * @return 
     */
    private String getStringWithLink(String post) {
        String ret = "";
        String path = mReq.getServletContext().getRealPath("/");
        path += "/files/" + groupid + "/";
        String[] a = post.split("[$][$]");
        for (String h : a) {
            if (isInGroupFiles(h)) {
                ret += "<a href='./files/" + groupid + "/" + h + "'>" + h + "</a>";
            } else {
                String[] split = h.split("\\s");
                String ris = "";
                for (String s : split) {
                    try {
                        URL url = new URL(s);
                        ris += "<a href='" + url + "'>" + url + "</a>";
                    } catch (MalformedURLException e) {
                        ris += s + " ";
                    }
                }
                ret += ris;
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

    private void connectToDatabase() {
        try {
            dbm = new DBManager(mReq);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
