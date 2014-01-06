<%-- 
    Document   : cambiopassword
    Created on : 5-gen-2014, 16.30.40
    Author     : luca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cambio password</title>
    </head>
    <body>
        <h1>Insert your new password</h1>
        <%@ include file="getMessage.jsp" %>
        <form action="changePass" method="POST">
            <input type="password" name="passwd" />
            <input type="password" name="confpasswd" />
            <input type="hidden" value="<%= request.getParameter("code") %>" name="code" />
<input type="submit">
        </form>
    </body>
</html>
