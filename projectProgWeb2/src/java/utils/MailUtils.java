/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.security.Security;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author forna
 */
public class MailUtils {

    private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private final static String USERNAME = "websender.project@gmail.com";
    private final static String PASSWORD = "hippogrifone792";
    private final static String PORT = "465";

    public static boolean sendMail(String dest, String subject, String text) {
        boolean success = true;

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", PORT);
        props.setProperty("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        Session session;
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        // — Create a new message –
        Message msg = new MimeMessage(session);
        // — Set the FROM and TO fields –

        try {
            System.err.println(dest);
            msg.setFrom(new InternetAddress(USERNAME + ""));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(dest, false));
            msg.setSubject(subject);
            msg.setText(text);
            msg.setSentDate(new Date());
            Transport.send(msg);

        } catch (MessagingException ex) {
            Logger.getLogger(MailUtils.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

}
