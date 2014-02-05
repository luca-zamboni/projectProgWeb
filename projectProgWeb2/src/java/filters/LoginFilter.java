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
import utils.Support;

/**
 * filtro che si occupa di proteggere il sito da utenti non loggati
 *
 * @author luca
 */
public class LoginFilter implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        UserBean bean = (UserBean) Support.getInSession((HttpServletRequest) request, SessionUtils.USER);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String url = ((HttpServletRequest) request).getRequestURI();

        //System.out.print("url " + url );
        if (bean == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath()+"/index");
        } else {
            chain.doFilter(request, response);
            return;
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
