/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Group;
import beans.Message;
import beans.UserBean;
import static beans.UserBean.DEFAULT_IMG_PATH;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.RequestUtils;
import utils.SessionUtils;
import utils.Support;

/**
 *
 * @author luca
 */
public class CloseGroup extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            
            DBManager dbm = Support.getDBManager(req);
            int groupid = Integer.parseInt((String) req.getParameter(RequestUtils.GROUP_ID));
            
            dbm.closeGroup(groupid);
            
            Message m = new Message(Message.MessageType.SUCCESS, 4);
            Support.forward(getServletContext(), req, resp, "/adminmoderatori", m);
        
        }catch(SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
