/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import beans.UserBean;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class ProtectFiles implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        DBManager dbm = Support.getDBManager(req);
        
        if (dbm==null || DBManager.con==null) {
            try {
                dbm = new DBManager(req);
                Support.putDBManager(req, dbm);
            } catch (SQLException ex) {
                Logger.getLogger(ProtectFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        StringBuffer uri = req.getRequestURL();
        String[] split = uri.toString().split("[/]");
        int i;
        for (i = 0; i < split.length; i++) {
            if (split[i].equals("files")) {
                break;
            }
        }
        i += 2;
        try {

            int gr = Integer.parseInt(split[i - 1]);
            UserBean user = (UserBean) Support.getInSession(req, SessionUtils.USER);
            if (!dbm.isPrivateGroup(gr) || user != null && (dbm.isInGroup(user.getUserID(), gr) || dbm.isModerator(user.getUserID()))) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(((HttpServletRequest) request).getContextPath() + "/home");
            }

        } catch (Exception e) {
            res.sendRedirect(((HttpServletRequest) request).getContextPath() + "/home");
        }

    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
