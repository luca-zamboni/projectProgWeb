<%-- 
    Document   : register
    Created on : Dec 30, 2013, 9:43:56 PM
    Author     : jibbo
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
        <title>Registrazione</title>
    </head>
    <body>
        <jsp:include page="includes/messagedisplayer.jsp" />
        <form action="register" method="POST" id="regform">
            <% out.println("<input placeholder=\"email\" type='email' name=" + RequestUtils.EMAIL + " required />"); %>
            <% out.println("<input pattern=\".{6,}\" title=\"Almeno 6 caratteri\" placeholder=\"username (Opzionale)\" type=\"text\" name=" + RequestUtils.USERNAME + " />"); %>
            <% out.println("<input pattern=\".{6,}\" title=\"Almeno 6 caratteri\" placeholder=\"password\" type=\"password\" name=" + RequestUtils.PASSWD + " />");%>
            <input type="submit" id="regBtn" value="Registrati"/>
        </form>
    </body>

</html>
