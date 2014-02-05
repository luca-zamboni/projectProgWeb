package filters;

import beans.Message;
import utils.SessionUtils;
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
import utils.Support;

/**
 * filtro che si occupa di proteggere il sito da utenti non loggati
 *
 * @author luca
 */
public class AddPostFilter implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        UserBean bean = (UserBean) ((HttpServletRequest) request).getSession().getAttribute(SessionUtils.USER);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = ((HttpServletRequest) request).getRequestURI();
        DBManager dbm = Support.getDBManager(httpRequest);
        String groupid = request.getParameter("gid");
        int gid = groupid == null ? -1 : Integer.parseInt(groupid);
        try {
            if (bean == null || (bean.getTypeToInt() == 0 && !dbm.isInGroup(bean.getUserID(), gid) && dbm.isPrivateGroup(gid))) {
                return;
            } else {
                try {
                    if (!dbm.isClosedGroup(gid)) {
                        chain.doFilter(request, response);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AddPostFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
