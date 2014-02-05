/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import beans.Message;
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
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class IsInGroupFilter implements Filter {

    private FilterConfig filterConfig = null;

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = ((HttpServletRequest) request).getRequestURI();

        int groupid = Integer.parseInt((String) req.getParameter(RequestUtils.GROUP_ID));
        try {
            DBManager dbm = Support.getDBManager(req);
            if (dbm == null || DBManager.con==null) {
                dbm = new DBManager(req);
                Support.putDBMangaer(req, dbm);
            }
            if (dbm.isPrivateGroup(groupid)) {
                UserBean bean = (UserBean) Support.getInSession(req, SessionUtils.USER);
                if (bean != null && dbm.isInGroup(bean.getUserID(), groupid)) {
                    chain.doFilter(request, response);
                } else {
                    Support.forward(req.getServletContext(), req, resp, "/login", new Message(Message.MessageType.ERROR, 6));
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddPostFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}
