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
 * @author luca
 */
public class LoginFilter implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        UserBean bean = (UserBean) Support.getInSession((HttpServletRequest)request, SessionUtils.USER);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = ((HttpServletRequest)request).getRequestURI();
        String op = request.getParameter("opcode");
        
        boolean condition = !(op!=null && (op.equals("login") || !op.equals("register") || !op.equals("forgot")));

        //System.out.print("url " + url );
        if (httpRequest.getMethod().equalsIgnoreCase("GET")) {
            if (bean == null && condition) {
                ((HttpServletResponse) response).sendRedirect("/ProjectProgWeb2/pages/lf?opcode=login");
            }else{
                chain.doFilter(request, response);
            }
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