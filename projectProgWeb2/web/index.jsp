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
        <title>Login</title>
        <%@ include file="header.jsp" %>
    </head>
    <body>
        <% if (request.getSession().getAttribute(SessionUtils.USER) == null) {%>
            
            <%@ include file="getMessage.jsp" %>
            <form action="login" method="POST">
                <% out.println("<input placeholder=\"username\" type=\"text\" name=" + RequestUtils.USERNAME + " />"); %>
                <% out.println("<input placeholder=\"password\" type=\"password\" name=" + RequestUtils.PASSWD + " />"); %>
                <input type="submit" value="Entra!" />
            </form> 
        <% } else { %>
            <jsp:forward page="home.jsp"/>
        <% }%>
        <a href="register.jsp">Registrati</a>
        <a href="forgetPassword.jsp">Passowrd dimenticata?</a>

    </body>
</html>

