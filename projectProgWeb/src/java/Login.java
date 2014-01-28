
import db.DBManager;
import html.HtmlHelper;
import static html.HtmlHelper.generateH;
import static html.HtmlHelper.getDateFromTimestamp;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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

/**
 * gestisce i dati passati in input nel form di login per autorizzare il browser
 * alla navigazione usando un cookie, nel caso di utente autorizzato genera la
 * pagina principale del sistema (home) in caso contrario reindirizza alla
 * pagina di login
 *
 * @author forna
 */
public class Login extends HttpServlet {

    //Session variables
    public static final String SESSION_USER = "username";
    public static final String SESSION_DATA = "data";
    public static final String SESSION_COOKIE = "cookie";

    private final String CAMPOSUSER = "username";
    private final String CAMPOPASS = "password";

    private String user;
    private String password;
    private DBManager dbm;
    private String lastLogin = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            connectToDatabase(request);
            HttpSession session = request.getSession();
            user = (String) session.getAttribute(SESSION_USER);
            generateHtml(request, response);
        } catch (IOException | SQLException e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            connectToDatabase(request);
            int login = loginValid(request);
            if (login == 0) {
                setDateCookie(request, response);
                checkAndSetSession(request);
                generateHtml(request, response);
            } else {
                response.sendRedirect("./?error=" + login);
            }
        } catch (IOException | SQLException e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void generateHtml(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        PrintWriter pw = response.getWriter();
        String body = "";

        lastLogin = getDateCookie(request, response);

        body += constructStringLogin();

        body += controlError(request);

        body += getYourPendings();

        body += getTableGroups();
        body += "</div>";

        pw.print(HtmlHelper.addHtml(body, user, dbm.getAvatar(dbm.getIdFromUser(user))));

    }

    private String getYourPendings() {
        String html = "", link = "";
        try {
            ArrayList<Group> grp = dbm.getAllPendingsGroups(user);
            if (grp.isEmpty()) {
                //html += Html.generateH(3, "You have no new invitation");
            } else {
                html += "<style> .table td {\n"
                        + "   text-align: center;   \n"
                        + "}"
                        + "</style>";
                html += "<br>" + generateH(3, "Your invitations");
                html += "<table class=\"table table-condensed table-hover\">";
                html += "<tr>";
                html += "<td><b>Owner</b></td>";
                html += "<td><b>Group Name</b></td>";
                html += "<td><b>Creation Date</b></td>";
                html += "<td><b>Accept/Decline</b></td>";
                html += "</tr>";

                for (Group aux : grp) {
                    link = "./AcceptInvitation?g=" + aux.getId();

                    html += "<tr>";
                    html += "<td>" + aux.getOwnerName() + "</td>";
                    html += "<td>" + aux.getGroupName() + "</td>";
                    html += "<td>" + getDateFromTimestamp(aux.getCreationDate()) + "</td>";
                    html += "<td>";
                    html += HtmlHelper.generateButton("Accept", link, "btn btn-success btn-xs", "ok");
                    html += " ";
                    html += HtmlHelper.generateButton("Decline", link + "&dec=1", "btn btn-danger btn-xs", "remove");
                    html += "</td>";

                    html += "</tr>";
                }
            }
        } catch (SQLException | ParseException e) {

        }
        return html + "</table>";
    }

    public String getAllGroups(ArrayList<Group> groups, String user) {
        String html = "";
        String haux = "";
        int i=0;
        if (groups.isEmpty()) {
            html += HtmlHelper.generateH(3, "You don't belong to any group");
            html += HtmlHelper.generateH(4, "Create your own");
            html += HtmlHelper.generateButton("Create Group", "./newGroup", "btn btn-success");
        } else {
            boolean ctrl = true;
            html += "<style> .table td {\n"
                    + "   text-align: center;   \n"
                    + "}"
                    + "</style>";
            haux += "" + generateH(3, "Your Groups");
            haux += "<table class=\"table table-condensed table-hover\">";
            haux += "<tr><td colspan=\"4\"><b>Groups changed after your last login</b></td></tr>";
            haux += "<tr>";
            haux += "<td><b>Owner</b></td>";
            haux += "<td><b>Group Name</b></td>";
            haux += "<td><b>Creation Date</b></td>";
            haux += "<td><b>Admin</b></td>";
            haux += "</tr>";
            html += "<table>";
            for (Group aux : groups) {
                long l=0;
                try{
                l = Long.parseLong(lastLogin);
                }catch(NumberFormatException e){
                    l=0;
                }
                if (aux.getLastChange() < l && ctrl) {
                    html +="</table>";
                    html += "<table class=\"table table-condensed table-hover\">";
                    html += "<tr><td colspan=\"4\"><b>Groups which did not change</b></td></tr>";
                    html += "<tr>";
                    html += "<td><b>Owner</b></td>";
                    html += "<td><b>Group Name</b></td>";
                    html += "<td><b>Creation Date</b></td>";
                    html += "<td><b>Admin</b></td>";
                    html += "</tr>";
                    ctrl = false;
                }
                if(ctrl){
                    html += "</table>";
                    html += haux;
                }
                if (aux.getOwnerName().equals(user)) {
                    html += "<tr class=\"success\">";
                    html += "<td>" + aux.getOwnerName() + "</td>";
                    html += "<td><a href=\"groupHome?g=" + aux.getId() + "\">" + aux.getGroupName() + "</a></td>";
                    html += "<td>" + getDateFromTimestamp(aux.getCreationDate()) + "</td>";
                    html += "<td><a href=\"newGroup?g=" + aux.getId() + "\"><button type=\"button\" class=\"btn btn-default btn-xs\">\n"
                            + "  <span class=\"glyphicon glyphicon-th\"></span> Manage\n"
                            + "</button></a></td>";
                } else {
                    html += "<tr>";
                    html += "<td>" + aux.getOwnerName() + "</td>";
                    html += "<td><a href=\"groupHome?g=" + aux.getId() + "\">" + aux.getGroupName() + "</a></td>";
                    html += "<td>" + getDateFromTimestamp(aux.getCreationDate()) + "</td>";
                    html += "<td><button type=\"button\" class=\"btn btn-danger btn-xs\">\n"
                            + "  <span class=\"glyphicon glyphicon-log-out\"></span> &nbsp;&nbsp;Leave&nbsp;\n"
                            + "</button></td>";
                }
                html += "</tr>";
            }
            html += "</table>";
        }
        return html;
    }

    private String getTableGroups() {
        ArrayList<Group> mGroups;
        String ret = "";
        try {
            mGroups = dbm.getAllGroups(user);
            ret = getAllGroups(mGroups, user);
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private String controlError(HttpServletRequest request) {
        String body = "";
        String error = request.getParameter("error");
        if (error != null && error.equals("10")) {
            try {
                int gid = Integer.parseInt(request.getParameter("g"));
                body += HtmlHelper.generateHWithColor(3, "You are not the owner of the group \"" + dbm.getGroupTitleById(gid)
                        + "\" \n!!", "text-danger");
            } catch (Exception e) {
            }
        }
        if (error != null && error.equals("11")) {
            try {
                int gid = Integer.parseInt(request.getParameter("g"));
                if (!dbm.isInGroup(dbm.getIdFromUser(user), gid)) {
                    body += HtmlHelper.generateHWithColor(3, "You are not invited in the group \"" + dbm.getGroupTitleById(gid)
                            + "\" \n!!", "text-danger");
                }
            } catch (NumberFormatException | SQLException e) {

            }
        }

        String acc = request.getParameter("acc");
        if (acc != null && acc.equals("1")) {
            int gid = Integer.parseInt(request.getParameter("g"));
            try {
                body += HtmlHelper.generateHWithColor(3, "Invite accepted at group \"" + dbm.getGroupTitleById(gid) + "\"\n", "text-success");
            } catch (SQLException e) {
            }
        }

        return body;
    }

    private void connectToDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String checkAndSetSession(HttpServletRequest request) {
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
     * @return 0 se giusto 1 se sbagliati 2 se minori di 6 caratteri 3 se errore
     * sql
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
        if (user.length() < 6 || password.length() < 6) {
            return 2;
        }
        try {
            if (!dbm.login(user, password)) {
                return 1;
            }
        } catch (SQLException ex) {
            System.out.print(ex.getErrorCode());
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            return 3;
        }

        return 0;

    }

    private String constructStringLogin() throws SQLException {
        String ret = "", aux = "";
        if (lastLogin.equals("")) {
            ret += "<h3>Primo accesso eseguito su questo pc</h3><h1>\n";
            ret += aux;
        } else {
            Date data = new Date();
            data.setTime(Long.parseLong(lastLogin));
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            String dateFormatted = formatter.format(data);
            ret += "<h3>Ultimo accesso eseguito il " + dateFormatted + "</h3><h1>";
            ret += aux;
        }
        return ret;
    }

    private String getDateCookie(HttpServletRequest request,
            HttpServletResponse response) {
        String ret = "";
        Cookie userCookie = getCookie(request, user);
        if (userCookie != null) {
            HttpSession session = request.getSession();
            ret = (String) session.getAttribute(SESSION_COOKIE);
            //ret = userCookie.getValue();
        }
        return ret;
    }

    private void setDateCookie(HttpServletRequest request,
            HttpServletResponse response) {

        String ret = "";
        Date a = new Date();
        Cookie userCookie = getCookie(request, user);
        if (userCookie != null) {
            ret = userCookie.getValue();
        }
        userCookie = new Cookie(user, a.getTime() + "");
        userCookie.setMaxAge(3600 * 24 * 30 * 6);
        response.addCookie(userCookie);
        
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_COOKIE, a.getTime() + "");

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
