/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

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
 * @author forna
 */
public class JspFilter implements Filter {

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse res = ((HttpServletResponse) response);
        HttpServletRequest req = ((HttpServletRequest) request);
        
        System.err.println("YOLO");

        if (req.getMethod().equalsIgnoreCase("GET")) {
            res.sendRedirect("/projectProgWeb2/index");
            System.err.println("YOLOGET");
        } else {
            chain.doFilter(request, response);
            System.err.println("YOLOOTHER");
        }

    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}
