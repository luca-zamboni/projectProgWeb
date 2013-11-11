
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class login extends HttpServlet {
    
    private final String CAMPOSUSER = "username";
    private final String CAMPOPASS = "password";
    
    private String user;
    private String password;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter pw = response.getWriter();
        Enumeration paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            pw.println(paramName+" "+paramValues[0]);
            if(paramName.equals(CAMPOSUSER)){
                user = paramValues[0];
            }
            if(paramName.equals(CAMPOPASS)){
                password = paramValues[0];
            }
            
        }
        
        //TODO check del login
        
        // solo debug per la data
        pw.print(setDateCookie(request, response, user));
        
    }
    
    private String setDateCookie(HttpServletRequest request,
            HttpServletResponse response,String user){
        
        String ret = "";
        Date a = new Date();
        Cookie userCookie = getCookie(request, user);
        if(userCookie != null){
            ret = userCookie.getValue();
        }
        userCookie = new Cookie(user, a.getTime() + "");
        response.addCookie(userCookie);
        return ret;
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

}
