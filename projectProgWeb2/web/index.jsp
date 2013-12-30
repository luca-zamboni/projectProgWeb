<%-- 
    Document   : index
    Created on : Dec 30, 2013, 8:23:28 PM
--%>

<%@page import="utils.SessionUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <% if(request.getSession().getAttribute(SessionUtils.USER)==null){ %>
            <h1><% out.println("Hello world!");%></h1>
        <% }else{ %>
        <jsp:forward page="home.jsp"/>
        <% } %>
    </body>
    <%@ include file="header.jsp" %>
</html>
