
import db.DBManager;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author luca
 */
public class FilterNotOwner implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try {
            String user = (String) ((HttpServletRequest) request).getSession().getAttribute(Login.SESSION_USER);
            String gpaux = (String) ((HttpServletRequest) request).getParameter("g");

            db.DBManager dbm = null;
            dbm = new DBManager((HttpServletRequest) request);

            if (gpaux == null || gpaux.equals("-1")) {
                
            } else if (dbm.getGroupOwnerById(Integer.parseInt(gpaux)) == dbm
                    .getIdFromUser(user)) {
                
            }else {
                ((HttpServletResponse) response).sendRedirect("./home?error=10&g="+gpaux);
            }

            chain.doFilter(request, response);

        } catch (Exception e) {

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
