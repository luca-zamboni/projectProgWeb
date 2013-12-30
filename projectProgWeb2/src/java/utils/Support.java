/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import beans.Message;
import java.io.IOException;
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

    public static void forward(ServletContext sc, HttpServletRequest request,
            HttpServletResponse response, String page) throws ServletException, IOException {
        RequestDispatcher rd = sc.getRequestDispatcher(page);
        rd.forward(request, response);
    }

    public static void addToSession(HttpServletRequest request, String name, Object value) {
        request.getSession(true).setAttribute(name, value);
    }
}
