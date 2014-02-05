/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.MailUtils;
import utils.RequestUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class ForgetPassword extends HttpServlet {

    DBManager dbm;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Support.forward(getServletContext(), req, resp, "/forgetPassword.jsp", null);
    }
    
    

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectDatabase(req);
        
        String user = (String) req.getParameter(RequestUtils.EMAIL);
        
        String code = Support.randomStringSHA1(10);
        String link = "http://localhost:8080/projectProgWeb2/changePass?code="+code;
        String newPass = Support.randomStringSHA1(10);   
        newPass = newPass.substring(0, 10);
        
        try{
            int id = dbm.getIdFromUser(user);
            if(id == -1){
                id = dbm.getIdFromMail(user);
            }
            String mail = dbm.getEmail(id);
            if(mail == null){
                req.getSession().setAttribute(RequestUtils.MESSAGE, new beans.Message(beans.Message.MessageType.ERROR, 5));
                Support.forward(getServletContext(), req, resp, "/forgetPassword.jsp", null);
            }else{
                
                String subject = "Cambio password";
                String text = "Se clikki sul link entro 90 secondi la tua nuova password sarà "+ newPass +" \n" + link;
                
                MailUtils.sendMail(mail, subject, text);
                
                dbm.setNewForgetPass(id, code,newPass,"" + (new Date().getTime() + 90));
                
                resp.sendRedirect("./");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ForgetPassword.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connectDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
