<%-- 
    Document   : creategroup
    Created on : Jan 9, 2014, 8:47:55 PM
    Author     : forna
--%>

<%@page import="utils.SessionUtils"%>
<%@page import="utils.Support"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.UserBean"%>
<%@page import="utils.RequestUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create the new group</title>
    </head>
    <body>
        <jsp:include page="includes/messagedisplayer.jsp" />
        <% UserBean us = (UserBean) Support.getInSession(request, SessionUtils.USER); %>
        <a href="home.jsp">home</a>
        <h1>Create the new Group</h1><br>
        <form action="groupCreate" method="POST" >
            <p>
                Private group?: <% out.println("<input type=\"checkbox\" name=\"" + RequestUtils.GROUP_PRIVATE + "\"/>"); %><br>
                Title of the group: <% out.println("<input type=\"text\" name=\"" + RequestUtils.GROUP_TITLE + "\"/>");%><br>
                <b>Scegli chi invitare:</b><br>
                <% ArrayList<UserBean> users = (ArrayList<UserBean>) request.getAttribute(RequestUtils.USERLIST); %>
                <%  if (users != null) { %>
                <%      for (UserBean user : users) {%>
                <%          if(us.getUserID()!=user.getUserID())out.println("<input type=\"checkbox\" name=\"" + RequestUtils.USERCHECK + "\" value=\"" 
                        + user.getUserID() + "\"<\\input><br>"+user.toString()); %>
                <%      } %>
                <%  } %>
                <input type="submit" id="regBtn" value="Aggiungi gruppo"/>
            </p>
        </form>
    </body>
</html>