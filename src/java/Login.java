
import db.DBManager;
import html.Html;
import static html.Html.generateH;
import static html.Html.getDateFromTimestamp;
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
        try {
            connectToDatabase(request);
            HttpSession session = request.getSession();
            user = (String) session.getAttribute(SESSION_USER);
            generateHtml(request, response);
        } catch (Exception e) {
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            connectToDatabase(request);
            int login = loginValid(request);
            if (login == 0) {
                checkAndSetSession(request);
                generateHtml(request, response);
            } else {
                response.sendRedirect("./?error=" + login);
            }
        } catch (Exception e) {
        }
    }

    private void generateHtml(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        PrintWriter pw = response.getWriter();
        String body = "";
        body += constructStringLogin(setDateCookie(request, response));
        body += "<a href='newGroup' type=\"button\" class=\"btn btn-primary btn-lg\">"
                + "Create Group"
                + "</a>";
        body += " " + Html.generateButton(" Carica avatar", "./uploadAvatar", "btn btn-primary btn-lg");

        body += controlError(request);

        body += getYourPendings();

        body += getTableGroups();
        body += "<a href='logout'>Logout</a></div>";
        
        pw.print(Html.addHtml(body,user));

    }

    private String getYourPendings() {
        String html = "", link = "";
        try {
            ArrayList<Group> grp = dbm.getAllPendingsGroups(user);
            if (grp.isEmpty()) {
                html += Html.generateH(3, "You have no new invitation");
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
                    html += Html.generateButton("Accept", link, "btn btn-success btn-xs", "ok");
                    html += " ";
                    html += Html.generateButton("Decline", link + "&dec=1", "btn btn-danger btn-xs", "remove");
                    html += "</td>";

                    html += "</tr>";
                }
            }
        } catch (Exception e) {

        }
        return html + "</table>";
    }

    private String getTableGroups() {
        ArrayList<Group> mGroups;
        String ret = "";
        try {
            mGroups = dbm.getAllGroups(user);
            ret = Html.getAllGroups(mGroups, user);

        } catch (SQLException ex) {
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
                body += Html.generateHWithColor(3, "You are not the owner of the group \"" + dbm.getGroupTitleById(gid)
                        + "\" \n!!", "text-danger");
            } catch (Exception e) {
            }
        }
        if (error != null && error.equals("11")) {
            try {
                int gid = Integer.parseInt(request.getParameter("g"));
                if(!dbm.isInGroup(dbm.getIdFromUser(user), gid))
                    body += Html.generateHWithColor(3, "You are not invited in the group \"" + dbm.getGroupTitleById(gid)
                        + "\" \n!!", "text-danger");
            } catch (Exception e) {
            }
        }

        String acc = request.getParameter("acc");
        if (acc != null && acc.equals("1")) {
            int gid = Integer.parseInt(request.getParameter("g"));
            try {
                body += Html.generateHWithColor(3, "Invite accepted at group \"" + dbm.getGroupTitleById(gid) + "\"\n", "text-success");
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

    private String constructStringLogin(String date) throws SQLException {
        String ret = "",aux="";
        String avatar = dbm.getAvatar(dbm.getIdFromUser(user));
        if (avatar != null && (!avatar.equals(""))) {
            aux += Html.getImageAvatarSmall(avatar);
        }
        if (date.equals("")) {
            ret += "<h3>Primo accesso eseguito su questo pc</h3><h1>\n";
            ret += aux;
            ret += " Welcome " + user + "</h1>";
        } else {
            Date data = new Date();
            data.setTime(Long.parseLong(date));
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String dateFormatted = formatter.format(data);
            ret += "<h3>Ultimo accesso eseguito il " + data.toString() + "</h3><h1>";
            ret += aux;
            ret += " Re-Welcome " + user + "</h1>\n";
        }
        return ret;
    }

    private String setDateCookie(HttpServletRequest request,
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
