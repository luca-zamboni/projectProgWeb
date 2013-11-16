
import db.DBManager;
import html.Html;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Group;

public class Login extends HttpServlet {

    //Session variables
    public static final String SESSION_USER = "username";
    public static final String SESSION_DATA = "data";

    private final String CAMPOSUSER = "username";
    private final String CAMPOPASS = "password";

    private String user;
    private String password;
    private DBManager dbm;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        connectToDatabase();
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute(SESSION_USER);
        if(username.equals("")){
            response.sendRedirect("./");
        }else{
            generateHtml(request,response,username);
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        connectToDatabase();
        int login = loginValid(request);
        if (loginValid(request) == 0) {
            checkAndSetSession(request,user);
            generateHtml(request, response, user);
        }else{
            response.sendRedirect("./?error="+login);
        }
    }
    
    private void generateHtml(HttpServletRequest request, HttpServletResponse response,String user) throws IOException{
        
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.print(Html.includeHead());
        pw.print("<body>");
        String body = "";
        body += constructStringLogin(setDateCookie(request, response, user),user);
        body += "<a href='newGroup' type=\"button\" class=\"btn btn-primary btn-lg\">"
                + "Create Group"
                + "</a>";
        body += getTableGroups(user);
        body +="<a href='logout'>Logout</a></div>";
        pw.print(Html.centerInPage(body));
        pw.println("</body>");
        pw.println("</html>");
        
    }
    
    private String getTableGroups(String user){
        ArrayList<Group> mGroups;
        String ret = "";
        try {
            mGroups = dbm.getAllGroups(user);
            ret = Html.getAllGroups(mGroups);
            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    
    private void connectToDatabase(){
      try {
          //cambiare qua cazzo
             dbm= new DBManager(DBManager.DB_URL);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    private String  checkAndSetSession(HttpServletRequest request,String user){
        HttpSession session = request.getSession();
        String usersession = (String) session.getAttribute(SESSION_USER);
        if (usersession == null) {
            setSessionParams(request, user, new Date());
        } else {
            if (!user.equals(usersession)) {
                setSessionParams(request, user, new Date());
            }
        }
        return "";
    }
    
    /**
     * 
     * @param request
     * @return 0 se giusto 1 se sbagliati 2 se minori di 6 caratteri 3 se errore sql
     */
    private int loginValid(HttpServletRequest request) {

        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramName.equals(CAMPOSUSER)) {
                user = paramValues[0];
            }
            if (paramName.equals(CAMPOPASS)) {
                password = paramValues[0];
            }
        }
        if(user.length() <6 || password.length() < 6)
            return 2;
        try {
            if(!dbm.login(user, password))
                return 1;
        } catch (SQLException ex) {
            System.out.print(ex.getErrorCode());
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            return 3;
        }
        
        return 0;
        
    }
    
    private String constructStringLogin(String date,String user){
        String ret = "";
        if (date.equals("")) {
            ret += "<br><h3>Primo accesso eseguito su questo pc</h3>";
            ret += "<h1> Welcome " + user + "</h1>";
        } else {
            Date data = new Date();
            data.setTime(Long.parseLong(date));
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String dateFormatted = formatter.format(data);
            ret += "<br><h3>Ultimo accesso eseguito il " + data.toString() +"</h3>";
            ret += "<h1> Re-Welcome " + user + "</h1>";
        }
        return ret;
    }

    private String setDateCookie(HttpServletRequest request,
            HttpServletResponse response, String user) {

        String ret = "";
        Date a = new Date();
        Cookie userCookie = getCookie(request, user);
        if (userCookie != null) {
            ret = userCookie.getValue();
        }
        userCookie = new Cookie(user, a.getTime() + "");
        userCookie.setMaxAge(3600 * 24 * 30 * 6);
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

    public static void setSessionParams(HttpServletRequest request, String username, Date d) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_USER, username);
        DateFormat df = new SimpleDateFormat("dd/M h:m");
        session.setAttribute(SESSION_DATA, df.format(d));
    }

}
