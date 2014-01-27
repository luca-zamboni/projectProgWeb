/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Message;
import beans.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
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
 * @author forna
 */
public class FrontController extends HttpServlet {

    private static final String OP_CODE = "opcode";

    private static final String BASEPATH = "/projectProgWeb2/";
    private static final String PAGEPATH = "pages/";
    private static final String JSPPATH = "/WEB-INF/jsp/";

    DBManager dbm;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String op = request.getParameter(OP_CODE);

        boolean notPublic = accessingToNotPublicFunctions(request, response);
        boolean loggedIn = isLoggedIn(request, response);

        if (loggedIn) {
            if (!notPublic || op == null) {
                op = "index";
            }
        } else {
            if (notPublic || op == null) {
                op = "login";
            }
        }

        System.err.println(op != null ? op : "null");

        if (op != null) {
            switch (op) {
                case "login":
                    Support.forward(getServletContext(), request, response, JSPPATH + "index.jsp", null);
                    break;
                case "logout":
                    this.logout(request, response);
                    break;
                case "register":
                    Support.forward(getServletContext(), request, response, JSPPATH + "register.jsp", null);
                    break;
                case "index":
                    Support.forward(getServletContext(), request, response, JSPPATH + "home.jsp", null);
                    break;
                case "profile":
                    Support.forward(getServletContext(), request, response, JSPPATH + "profile.jsp", null);
                    break;
                case "creategroup":
                    Support.forward(getServletContext(), request, response, JSPPATH+"", null);
                case "forgot":
                //todo
                default:
                    response.sendRedirect(BASEPATH + PAGEPATH + "in?opcode=index");
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String op = request.getParameter(OP_CODE);

        switch (op) {
            case "login":
                login(request, response);
                break;
            case "logout":
                this.logout(request, response);
                break;
            case "register":
                this.register(request, response);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="UTILITY FILTRATING FUNCTIONS">
    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) {

        UserBean bean = (UserBean) Support.getInSession((HttpServletRequest) request, SessionUtils.USER);

        return (bean != null && (bean instanceof UserBean));
    }

    private boolean accessingToNotPublicFunctions(HttpServletRequest request, HttpServletResponse response) {
        boolean ret = true;

        String op = request.getParameter(OP_CODE);

        return !(op != null && (op.equals("login") || op.equals("register") || op.equals("forgot")));
    }//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="LOGIN CODE HERE">
    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        connectDatabase(req);
        
        String[] credentials = getCredentials(req);
        String page = "/WEB-INF/jsp/index.jsp";
        UserBean login = getUser(req, credentials[0], credentials[1]);
        Message msg = null;
        
        if (login.getUserID() >= 0) {
            Support.addToSession(req, SessionUtils.USER, login);
            Support.addToSession(req, SessionUtils.DBM, dbm);
            resp.sendRedirect("/projectProgWeb2/pages/index?opcode=index");
        } else {
            msg = new Message(Message.MessageType.ERROR, 0);
            req.setAttribute(RequestUtils.MESSAGE, msg);
            Support.forward(getServletContext(), req, resp, page, msg);
        }

    }

    public String[] getCredentials(HttpServletRequest request) {

        String[] out = new String[]{null, null};
        Enumeration<String> paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);

            if (paramName.equals(RequestUtils.USERNAME)) {
                out[0] = paramValues[0];
            }

            if (paramName.equals(RequestUtils.PASSWD)) {
                out[1] = paramValues[0];
            }

        }
        return out;
    }

    /**
     *
     * @return null se errore server, -1 se non lo trova id dell'utente
     * altrimenti
     */
    private UserBean getUser(HttpServletRequest request, String username, String password) {

        try {
            dbm = new DBManager(request);
            UserBean out = dbm.login(username, password);
            out.setAvatar(dbm.getAvatar(out.getUserID()));
            return out;
        } catch (SQLException ex) {
        }
        return new UserBean(-1, 0, "", 0);
    }

    public void connectDatabase(HttpServletRequest request) {

        try {
            dbm = new DBManager(request);
            Support.putDBMangaer(request, dbm);
        } catch (SQLException ex) {
        }
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LOGOUT CODE HERE">
    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Support.removeFromSession(request, SessionUtils.USER);
        response.sendRedirect(BASEPATH + PAGEPATH + "lo?opcode=login");
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGISTRATION CODE HERE">
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] params = getParamsRegistation(request);

        int code = 0;
        boolean sanitized = checkInput(params, code);
        boolean isInserted = false;
        if (sanitized) {
            isInserted = insertUser(request, params);
        }
        Message msg = buildMessage(code, isInserted);
        if (msg.getType() == Message.MessageType.ERROR) {
            Support.forward(getServletContext(), request, response, "/WEB-INF/jsp/register.jsp", msg);
        } else {
            Support.forward(getServletContext(), request, response, "/WEB-INF/jsp/index.jsp", msg);
        }

    }

    private String[] getParamsRegistation(HttpServletRequest request) {
        String[] out = new String[]{null, null, null};
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramName.equals(RequestUtils.USERNAME)) {
                out[0] = paramValues[0];
            }
            if (paramName.equals(RequestUtils.PASSWD)) {
                out[1] = paramValues[0];
            }
            if (paramName.equals(RequestUtils.EMAIL)) {
                out[2] = paramValues[0];
            }
        }
        if (out[0] == null || out[0].length() < 1) {
            out[0] = out[2];
        }
        return out;
    }

    private boolean checkInput(String[] params, int code) {
        if (!Support.isInputValid(params[0], 5)) {
            code = 1;
            return false;
        }
        if (!Support.isInputValid(params[1], 5)) {
            code = 2;
            return false;
        }
        if (!Support.isEmailValid(params[2])) {
            code = 3;
            return false;
        }
        code = 0;
        return true;
    }

    private Message buildMessage(int code, boolean inserted) {

        //non e' riuscito perche' la mail/username gia' esiste
        if (!inserted) {
            return new Message(Message.MessageType.ERROR, code);
        }
        //everything is ok
        return new Message(Message.MessageType.SUCCESS, 0);
    }

    private boolean insertUser(HttpServletRequest request, String[] params) {
        try {
            DBManager dBManager = new DBManager(request);
            return dBManager.insertUser(params[0], params[1], params[2]);
        } catch (SQLException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }//</editor-fold>

}
