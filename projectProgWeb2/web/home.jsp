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
        <%@ include file="header.jsp" %>
        <title>Home</title>
    </head>
    <body>
        <h1>Ciao belli</h1>
        <p><%= Support.getDateFromTime(((UserBean) request.getSession().getAttribute(SessionUtils.USER)).getLastLogin()) %></p>
        <p><a href="./logout">Logout</a></p>
    </body>
</html>
