
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

/**
 *
 * @author luca
 */
public class Index extends HttpServlet {
    
    private DBManager dbm;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectToDatabase(req);
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute(Login.SESSION_USER);
        try {
            if (username.equals("")) {
                generateHtml(req, resp, username);
            } else {
                resp.sendRedirect("./home");
            }
        } catch (NullPointerException | IOException e) {
            generateHtml(req, resp, username);
        }
    }

    private void generateHtml(HttpServletRequest req, HttpServletResponse resp, String username) throws IOException {
        PrintWriter pw = resp.getWriter();
        String error ="";
        String gpaux = (String) req.getParameter("error");
        if(gpaux != null && gpaux.equals("1"))
            error += Html.generateHWithColor(3, "Login invalid!!", "text-danger");
        pw.print("<!DOCTYPE html>\n" +
"<!--\n" +
"To change this license header, choose License Headers in Project Properties.\n" +
"To change this template file, choose Tools | Templates\n" +
"and open the template in the editor.\n" +
"-->\n" +
"<html>\n" +
"    <head>\n" +
"        <title>TODO supply a title</title>\n" +
"        <meta charset=\"UTF-8\">\n" +
"        <link href=\"./css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
"        <script src=\"https://code.jquery.com/jquery.js\"></script>\n" +
"        <script src=\"http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.js\"></script>\n" +
"        <script src=\"./js/bootstrap.min.js\"></script>\n" +
"        <script type=\"text/javascript\">\n" +
"            $(document).ready(function () {\n" +
"                $(\"form\").validate({\n" +
"                    rules: {\n" +
"                        username: {\n" +
"                            required: true,\n" +
"                            minlength: 6\n" +
"                        },\n" +
"                        password: {\n" +
"                            required: true,\n" +
"                            minlength: 6\n" +
"                        }\n" +
"                    },\n" +
"                    highlight: function(element) {\n" +
"                        $(element).closest('.form-group').addClass('has-error');\n" +
"                    },\n" +
"                    unhighlight: function(element) {\n" +
"                        $(element).closest('.form-group').removeClass('has-error');\n" +
"                    },\n" +
"                    errorElement: 'span',\n" +
"                    errorClass: 'help-block',\n" +
"                    errorPlacement: function(error, element) {\n" +
"                        if(element.parent('.input-group').length) {\n" +
"                            error.insertAfter(element.parent());\n" +
"                        } else {\n" +
"                            error.insertAfter(element);\n" +
"                        }\n" +
"                    }\n" +
"                });\n" +
"            });\n" +
"        </script>\n" +
"    </head>\n" +
"    <body>\n" +
"        <div style=\"width:600px; margin:0 auto\">\n" +
"            <h1 style=\"text-align: center;\">Login</h1>\n" +
                error +
"            <form action=\"home\" method=\"POST\" class=\"form-horizontal\" role=\"form\">\n" +
"                <div class=\"form-group\">\n" +
"                    <label class=\"col-sm-2 control-label\">UserName</label>\n" +
"                    <div class=\"col-sm-10\">\n" +
"                        <input id=\"username\" name=\"username\" type=\"text\" class=\"form-control\" placeholder=\"Username\">\n" +
"                    </div>\n" +
"                </div>\n" +
"                <div class=\"form-group\">\n" +
"                    <label class=\"col-sm-2 control-label\">Password</label>\n" +
"                    <div class=\"col-sm-10\">\n" +
"                        <input id=\"password\" name=\"password\" type=\"password\" class=\"form-control\" id=\"inputPassword3\" placeholder=\"Password\">\n" +
"                    </div>\n" +
"                </div>\n" +
"                <div class=\"form-group\">\n" +
"                    <div class=\"col-sm-offset-2 col-sm-10\">\n" +
"                        <button type=\"submit\" class=\"btn btn-default\">Sign in</button>\n" +
"                    </div>\n" +
"                </div>\n" +
"            </form>\n" +
"\n" +
"        </div>\n" +
"    </body>\n" +
"</html>\n" +
"");
    }
    
    private void connectToDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
