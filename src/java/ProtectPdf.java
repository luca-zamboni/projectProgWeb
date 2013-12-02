
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Protegge il pdf da tentativi d'accesso da parte di chiunque tranne
 * il proprietario
 * @author forna
 */
public class ProtectPdf implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        StringBuffer uri = req.getRequestURL();
        String[] split = uri.toString().split("[/]");
        int i;
        for (i = 0; i < split.length; i++) {
            if (split[i].equals("pdf")) break;
        }
        i+=2;
        try{
            
            db.DBManager dbm = null;
            dbm = new DBManager((HttpServletRequest) request);
            
            int gr = Integer.parseInt(split[i]);
            String user = (String) ((HttpServletRequest) request).getSession().getAttribute(Login.SESSION_USER);
            System.err.println(dbm.isInGroup(dbm.getIdFromUser(user), gr));
            if(dbm.getGroupOwnerById(gr) == dbm.getIdFromUser(user)){
                chain.doFilter(request, response);
            }else{
                res.sendRedirect("../../home");
            }
        }catch(Exception e){
            res.sendRedirect("../../home");
        }
    }

    @Override
    public void destroy() {}
    
}
