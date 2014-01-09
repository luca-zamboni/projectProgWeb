<%-- 
    Document   : cambiopassword
    Created on : 5-gen-2014, 16.30.40
    Author     : luca
--%>

<%@page import="utils.Support"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file="includes/header.jsp" %>
        <title>Forgot password?</title>
    </head>
    <body>
        <h1>Insert your username or email</h1>
        <jsp:include page="includes/messagedisplayer.jsp" />
        <form action="ForgetPassword" method="POST">
            <input type="text" name="mail" />
            <button type="submit" class="btn-default">Confirm</button>
        </form>
    </body>
</html>
