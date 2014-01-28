/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author jibbo
 */
public class OwnerFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        UserBean bean = (UserBean) Support.getInSession((HttpServletRequest) request, SessionUtils.USER);

        DBManager dbm = Support.getDBMangaer((HttpServletRequest) request);
        if (bean == null || dbm == null) {
            ((HttpServletResponse) response).sendRedirect("./home");
            return;
        }

        String groupid = request.getParameter(RequestUtils.GROUP_ID);
        if (groupid == null) {
            chain.doFilter(request, response);
            return;
        }

        try {

            if (dbm.getGroupOwnerById(Integer.parseInt(groupid)) == bean.getUserID()) {
                chain.doFilter(request, response);
                return;
            }

            ((HttpServletResponse) response).sendRedirect("./home");

            /**
             * Destroy method for this filter
             */
        } catch (SQLException ex) {
            Logger.getLogger(OwnerFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
