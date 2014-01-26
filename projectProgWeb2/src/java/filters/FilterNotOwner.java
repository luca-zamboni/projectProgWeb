/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import beans.Message;
import beans.UserBean;
import java.io.IOException;
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
 * @author forna
 */
public class FilterNotOwner implements Filter {

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        try {

            UserBean user = (UserBean) req.getSession().getAttribute(SessionUtils.USER);
            String groupId = req.getParameter(RequestUtils.GROUP_ID);
            
            DBManager dbm = null;
            dbm = new DBManager(req);

            System.err.println(user);
            System.err.println(groupId);
            System.err.println(dbm.getGroupOwnerById(user.getUserID()));
            
            if (groupId==null || user==null) {
                throw new NullPointerException("cannot get the parameters");
            } else if (dbm.getGroupOwnerById(Integer.parseInt(groupId)) != user.getUserID()) {
                res.sendRedirect("");
            } else {
                chain.doFilter(request, response);
            }
            
        } catch (Exception ex) {
            res.sendRedirect("");
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}
