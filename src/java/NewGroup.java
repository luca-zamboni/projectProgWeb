
import db.DBManager;
import html.Html;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NewGroup extends HttpServlet {
    
    private DBManager dbm;
    private static final String USERCHECKBOX ="users";
    private static final String TITLE ="title-group";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            connectToDatabase();
            HttpSession session = req.getSession();
            String username = (String) session.getAttribute(Login.SESSION_USER);
            if(username.equals("")){
                resp.sendRedirect("./");
            }else{
                generateHtml(req,resp,username);
            }
        }catch(IOException e){
            resp.sendRedirect("./");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectToDatabase();
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute(Login.SESSION_USER);
        if(username.equals("")){
            resp.sendRedirect("./");
        }else{
            String title = req.getParameter(TITLE);
            String[] users = req.getParameterValues(USERCHECKBOX);
            
            if(checkParameter(title,users)){
                try {
                    dbm.newGroup(title, users, dbm.getIdFromUser(username));
                    resp.sendRedirect("./login");
                } catch (SQLException ex) {
                    Logger.getLogger(NewGroup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                resp.sendRedirect("./newGroup?err=1");
            }
            
        }
        
    }
    
    private boolean checkParameter(String title,String[] users) {  
        try{
            for(String i : users){
                
            }
        }catch(Exception e){
            return false;
        }
        return (title.length() > 4);
        
    }
    
    private void generateHtml(HttpServletRequest request, HttpServletResponse response,String user) throws IOException{
        
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.print(Html.includeHead());
        pw.print("<body>");
        pw.print(Html.centerInPage(generateStringBody(request, response, user)));
        pw.println("</body>");
        pw.println("</html>");
        
    }
    
    private String generateStringBody(HttpServletRequest request, HttpServletResponse response,String user){
        String body = "";
        String form = "";
        
        body += Html.h1String("Create a new Group");
        
        form = Html.generateForm("./newGroup", Html.POST, getStringForm());
        body += form;
        
        return body;
    }
    
    public String getStringForm(){
        String form = "";
        form += Html.generateH(3, "Group's title")
                + "<input name='title-group' type=\"text\" class=\"form-control\" placeholder=\"Title\">";
        form += Html.generateH(3, "Segli chi invitare");
        ///form += "<label class=\"checkbox\">";
        try {
            for(String i : dbm.getAllUSer()){
                form +="<input type='checkbox' name=\"users\" value='"+ i +"'>" + i + "<br>";
            }
        } catch (SQLException ex) {
            Logger.getLogger(NewGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        //form += "</label>";
        form+="<button type=\"submit\" class=\"btn btn-default\">Submit</button>";
        return form;
    }
    
    private void connectToDatabase(){
      try {
          //cambiare qua cazzo
             dbm= new DBManager(DBManager.DB_URL);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
}
