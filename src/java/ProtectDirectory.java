
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * protegge le cartelle del programma reindirizzando tutto a home
 * in particolare noi abbiamo usato questo filtro per proteggere il db
 * @author luca
 */
public class ProtectDirectory implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        ((HttpServletResponse) response).sendRedirect("../home");
        
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
