<%-- 
    Document   : home
    Created on : Dec 30, 2013, 9:00:44 PM
--%>

<%@page import="utils.Support"%>
<%@page import="beans.Message"%>
<%@page import="beans.UserBean" %>
<%@page import="utils.RequestUtils"%>
<%@page import="utils.SessionUtils"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <title>Home</title>
    </head>
    <body>
        <jsp:include page="includes/messagedisplayer.jsp" />
        <% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER); %>
        <h1>Ciao belli! sono </h1><a href="profile.jsp"><%= user.getUsername() %></a>
        <p><%= Support.getDateFromTime(user.getLastLogin()) %></p>
        <p><a href="creategroup.jsp">Crea gruppo!</a></p>
        <p><a href="./logout">Logout</a></p>
    </body>
</html>
