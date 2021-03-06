/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * filtro usato per proteggere i file da utenti esterni al gruppo in 
 * cui sono stati inseriti
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
            
            db.DBManager dbm = null;
            dbm = new DBManager((HttpServletRequest) request);
            
            int gr = Integer.parseInt(split[i-1]);
            String user = (String) ((HttpServletRequest) request).getSession().getAttribute(Login.SESSION_USER);
            System.err.println(dbm.isInGroup(dbm.getIdFromUser(user), gr));
            if(dbm.isInGroup(dbm.getIdFromUser(user), gr)){
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
