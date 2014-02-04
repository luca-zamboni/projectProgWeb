package filters;

import utils.SessionUtils;
import beans.UserBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;

/**
 * filtro che si occupa di proteggere il sito da utenti non loggati
 *
 * @author luca
 */
public class FileAccess implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        DBManager dbm = (DBManager) req.getSession().getAttribute(SessionUtils.DBM);
        UserBean ub = (UserBean) req.getSession().getAttribute(SessionUtils.USER);
        
        //TODO filter code
        
        String path = request.getServletContext().getContextPath();
        res.sendRedirect(path+"/index.jsp");

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
