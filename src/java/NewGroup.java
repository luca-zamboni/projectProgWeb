
import db.DBManager;
import html.Html;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NewGroup extends HttpServlet {

    private DBManager dbm;
    private static final String USERCHECKBOX = "users";
    private static final String GROUP = "group";
    private static final String TITLE = "title-group";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            connectToDatabase();
            HttpSession session = req.getSession();
            String username = (String) session.getAttribute(Login.SESSION_USER);
            generateHtml(req, resp, username);
        } catch (IOException e) {
            
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectToDatabase();
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute(Login.SESSION_USER);
        String title = req.getParameter(TITLE);
        String group = req.getParameter(GROUP);
        String[] users = req.getParameterValues(USERCHECKBOX);
        int check = checkParameter(title, users);
        if (check == 0) {
            System.out.print(group);
            try {
                if(group.equals("-1")){
                    //System.out.print(group);
                    dbm.newGroup(title, users, dbm.getIdFromUser(username));
                }
                else{
                    int aux = Integer.parseInt(group);
                    int owid = dbm.getGroupOwnerById(aux);
                    if (owid==dbm.getIdFromUser(username)) {
                        dbm.updateGroup(aux, title, users, username);
                    } else {
                        resp.sendRedirect("./home?error=10&g="+aux);
                    }
                }
                resp.sendRedirect("./home");
            } catch (SQLException ex) {
                Logger.getLogger(NewGroup.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (group == null)
                resp.sendRedirect("./newGroup?err=" + check);
            else 
                resp.sendRedirect("./newGroup?err=" + check+"&g="+group);
        }
    }

    private int checkParameter(String title, String[] users) {
        if (title.length() < 4) {
            return 1;
        }
        try {
            for (String i : users) {

            }
        } catch (Exception e) {
            return 2;
        }
        return 0;

    }

    private void generateHtml(HttpServletRequest request, HttpServletResponse response, String user) throws IOException {
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.print(Html.includeHead());
        pw.print("<body>");
        int i;        
        try {
            pw.print(Html.centerInPage(generateStringBody(request, response, user)));
        } catch (Exception e){
            e.printStackTrace();
            pw.println("\t"+e.getClass());
        }
        pw.println("</body>");
        pw.println("</html>");
    }

    private String generateStringBody(HttpServletRequest request, HttpServletResponse response, String user) throws Exception {
        String body = "";
        String form = "";

        String gpaux = (String) request.getParameter("g");
        if(gpaux == null || gpaux.equals("-1"))
            body += Html.h1String("Create a new Group");
        else if (dbm.getGroupOwnerById(Integer.parseInt(gpaux)) == dbm
                    .getIdFromUser(user))
            body += Html.h1String("Manage this group");
        else {
            response.sendRedirect("./home?error=10&g="+gpaux);
        }

        try{
          form = Html.generateForm("./newGroup", Html.POST, getStringForm(user, request));
        }catch(SQLException e){
            form = "Something gones wrong";
        }
        body += form;

        return body;
    }

    public String getStringForm(String user, HttpServletRequest request) throws SQLException {

        String err = (String) request.getParameter("err");
        String gpaux = (String) request.getParameter("g");
        
        int gp = -1;
        if(gpaux != null)
            gp = Integer.parseInt(gpaux);

        if (err == null) {
            err = "-1";
        }
        String form = "";

        form += "<input  type='hidden' style='visibility:hidden' name='group' value='"+gp+"'>";
        if (err.equals("1")) {
            form += Html.generateH(3, "Group's title")
                    + "<div class=\"form-group has-error\">"
                    + "<input name='title-group'  value=\""+dbm.getGroupTitleById(gp)
                    + "\" type=\"text\" class=\"form-control\" placeholder=\"Title\">"
                    + "</div>";
        } else {
            form += Html.generateH(3, "Group's title")
                    + "<input name='title-group' value=\""+dbm.getGroupTitleById(gp)
                    + "\" type=\"text\" class=\"form-control\" placeholder=\"Title\">";
        }
        form += Html.generateH(3, "Segli chi invitare");
        try {
            if (err.equals("2")) {
                form += html.Html.generateHWithColor(3, "Add at least one user","text-danger");
                for (String i : dbm.getAllUSer()) {
                    String checked = (dbm.isInGroup(dbm.getIdFromUser(i), gp)) ? "checked" : "";
                    if (!user.equals(i)) {
                        form += "<input type='checkbox' name=\"users\" "+checked+" value='" + i + "'>" + i + "<br>";
                    }
                }
            } else {
                for (String i : dbm.getAllUSer()) {
                    String checked = (dbm.isInGroup(dbm.getIdFromUser(i), gp)) ? "checked" : "";
                    if (!user.equals(i)) {
                        form += "<input type='checkbox' name=\"users\" "+checked+" value='" + i + "'>" + i + "<br>";
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        //form += "</label>";
        form += "<br><button type=\"submit\" class=\"btn btn-default\">Submit</button>";

        return form;
    }

    private void connectToDatabase() {
        try {
            dbm = new DBManager();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
