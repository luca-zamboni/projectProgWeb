<%-- 
    Document   : register
    Created on : Dec 30, 2013, 9:43:56 PM
    Author     : jibbo
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
        <title>Registrazione</title>
    </head>
    <body>
        <% if (request.getSession().getAttribute(SessionUtils.USER) == null) {
                Message msg = (Message) request.getAttribute(RequestUtils.MESSAGE);%>

        <p><%= msg == null ? "" : msg.toString()%></p>
        <form action="register" method="POST" id="regform">
            <% out.println("<input placeholder=\"email\" type='email' name=" + RequestUtils.EMAIL + " required />"); %>
            <% out.println("<input pattern=\".{6,}\" title=\"Almeno 6 caratteri\" placeholder=\"username (Opzionale)\" type=\"text\" name=" + RequestUtils.USERNAME + " />"); %>
            <% out.println("<input pattern=\".{6,}\" title=\"Almeno 6 caratteri\" placeholder=\"password\" type=\"password\" name=" + RequestUtils.PASSWD + " />"); %>
            <input type="submit" id="regBtn" value="Registrati"/>
        </div> 
        <% } else { %>
            <jsp:forward page="home.jsp"/>
        <% }%>
    </body>
    <%@ include file="header.jsp" %>
<!--    <script type="text/javascript" src="js/jquery-ui-1.10.3.custom.min.js"></script>
    <script type="text/javascript" src="js/registration.js"></script>-->
</html>
