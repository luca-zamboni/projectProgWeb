<%-- 
    Document   : index
    Created on : Dec 30, 2013, 8:23:28 PM
--%>

<%@page import="beans.Message"%>
<%@page import="beans.UserBean" %>
<%@page import="utils.RequestUtils"%>
<%@page import="utils.SessionUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <% if (request.getSession().getAttribute(SessionUtils.USER) == null) {
                Message msg = (Message) request.getAttribute(RequestUtils.MESSAGE);%>

        <p><%= msg == null ? "" : msg.toString()%></p>
        <form action="login" method="POST">
            <% out.println("<input placeholder=\"username\" type=\"text\" name=" + RequestUtils.USERNAME + " />"); %>
            <% out.println("<input placeholder=\"password\" type=\"password\" name=" + RequestUtils.PASSWD + " />"); %>
            <input type="submit" value="Entra!" />
        </form> 
        <% } else { %>
            <jsp:forward page="home.jsp"/>
        <% }%>
    </body>
    <%@ include file="header.jsp" %>
</html>
