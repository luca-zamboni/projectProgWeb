/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import beans.Message;
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
import utils.RequestUtils;
import utils.SessionUtils;

/**
 *
 * @author jibbo
 */
public class AlreadyLogged implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        UserBean bean = (UserBean) ((HttpServletRequest) request).getSession().getAttribute(SessionUtils.USER);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = ((HttpServletRequest) request).getRequestURI();
        String op = request.getParameter("opcode");
        
        boolean condition = op!=null && (op.equals("login") || op.equals("register") || op.equals("forgot"));

        if (bean == null && condition) { // non e' loggato
            chain.doFilter(request, response);
        } else {
            request.setAttribute(RequestUtils.MESSAGE, new Message(Message.MessageType.ERROR,6));
            ((HttpServletResponse) response).sendRedirect("/ProjectProgWeb2/pages/al?opcode=index");
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
