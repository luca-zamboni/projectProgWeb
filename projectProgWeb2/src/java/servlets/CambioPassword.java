/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Security;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import utils.RequestUtils;
import utils.SessionUtils;

/**
 *
 * @author luca
 */
public class CambioPassword extends HttpServlet {

    DBManager dbm;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectDatabase(req);
        String user = (String) req.getParameter(RequestUtils.EMAIL);

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        final String username = "lucazamboni92@gmail.com";
        final String password = "****";
        Session session;
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        
        try{
            int id = dbm.getIdFromUser(user);
            String mail = dbm.getEmail(id);
            if(mail == null){
                req.getSession().setAttribute(RequestUtils.MESSAGE, new beans.Message(beans.Message.MessageType.ERROR, 4));
                resp.sendRedirect("./cambiopassword.jsp");
            }else{
                // — Create a new message –
                Message msg = new MimeMessage(session);
                // — Set the FROM and TO fields –
                msg.setFrom(new InternetAddress(username + ""));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(mail , false));
                msg.setSubject("Cambio password");
                msg.setText("Cambio password " + new Date());
                msg.setSentDate(new Date());
                Transport.send(msg);
            }
        } catch (MessagingException | SQLException ex) {
            Logger.getLogger(CambioPassword.class.getName()).log(Level.SEVERE, null, ex);
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
