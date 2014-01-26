/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import beans.Message;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jibbo
 */
public class Support {

    protected static char[] goodChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
        'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
        'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '2', '3', '4', '5', '6', '7', '8', '9',};

    public static void forward(ServletContext sc, HttpServletRequest request,
            HttpServletResponse response, String page, Message msg) throws ServletException, IOException {
        RequestDispatcher rd = sc.getRequestDispatcher(page);
        if(msg!=null)
            request.setAttribute(RequestUtils.MESSAGE, msg);
        rd.forward(request, response);
    }

    public static String getDateFromTime(long date) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
    }

    public static void addToSession(HttpServletRequest request, String name, Object value) {
        request.getSession(true).setAttribute(name, value);
    }

    public static void removeFromSession(HttpServletRequest request, String name) {
        request.getSession(true).setAttribute(name, null);
    }

    public static boolean isInputValid(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean isInputValid(String s, int minLength) {
        return s != null && s.length() > minLength;
    }

    public static boolean isEmailValid(String mail) {
        Pattern rfc2822 = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        );
        return rfc2822.matcher(mail).matches();
    }

    public static String randomStringSHA1(int lenght) {
        try {
            Random r = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < lenght; i++) {
                sb.append(goodChar[(int) r.nextInt(lenght)]);
            }
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = sb.toString().getBytes();
            md.update(buffer);
            byte[] digest = md.digest();

            String hexStr = "";
            for (int i = 0; i < digest.length; i++) {
                hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
            }
            return hexStr;
        } catch (Exception e) {
            return "";
        }
    }

    public static Message getMessageInRequest(HttpServletRequest req) {
        Message msg = (Message) req.getAttribute(RequestUtils.MESSAGE);
        return msg;
    }
    
    public static Object getInSession(HttpServletRequest req,String key){
        return req.getSession().getAttribute(key);
    } 

    public static String getDateFromTimestampLong(String timestamp) {
        long l = Long.parseLong(timestamp);
        String dateAsText = new SimpleDateFormat("dd-MM-yyy HH:mm:ss")
                .format(new Date(l));
        return dateAsText;
    }
    
    public static void putDBMangaer(HttpServletRequest request, DBManager dbm){
        addToSession(request, SessionUtils.DBM, dbm);
    }
    
    public static DBManager getDBMangaer(HttpServletRequest request){
        return (DBManager) request.getSession().getAttribute(SessionUtils.DBM);
    }
    
}
