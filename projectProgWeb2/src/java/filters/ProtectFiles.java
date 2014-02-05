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
        
        StringBuffer uri = req.getRequestURL();
        String[] split = uri.toString().split("[/]");
        int i;
        for (i = 0; i < split.length; i++) {
            if (split[i].equals("files")) break;
        }
        i+=2;
        try{
            
            DBManager dbm = Support.getDBManager(req);
            
            int gr = Integer.parseInt(split[i-1]);
            UserBean user = (UserBean) Support.getInSession(req, SessionUtils.USER);
            System.err.println(dbm.isInGroup(user.getUserID(), gr));
            if(dbm.isInGroup(user.getUserID(), gr)){
                chain.doFilter(request, response);
            }else{
                res.sendRedirect("../home");
            }
            
        }catch(Exception e){
            res.sendRedirect("../home");
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
