/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import db.DBManager;
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
    private String user;
    private HttpServletRequest mReq;
    private HttpServletResponse mResp;

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

            PrintWriter pw = resp.getWriter();
            String post = req.getParameter("post");
            if (post == null || post.equals("")) {
                String referer = req.getHeader("Referer");
                resp.sendRedirect(referer);
            } else {
                String aux = post.replace(" ", "");
                if (aux.equals("")) {
                    String referer = req.getHeader("Referer");
                    resp.sendRedirect(referer);
                } else {
                    dbm.insertPost(dbm.getIdFromUser(user), groupid, post);
                    

                    for (String t : getAllLinkedFile(post)) {
                        pw.print(t);
                    }
                    pw.print(post);
                }
            }

            // queste due istruzioni rimandano alla pagina precedente
            String referer = req.getHeader("Referer");
            resp.sendRedirect(referer);

        } catch (Exception e) {
            PrintWriter pw = resp.getWriter();
            pw.print("Error -- Something goes wrong -- Tips: Format your PC\n");
            pw.print(e.toString());
        }

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
