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
        <h1>Insert your username or email</h1>
        <%@ include file="getMessage.jsp" %>
        <form action="cambioPassword" method="POST">
            <input type="text" name="mail" />
        </form>
    </body>
</html>
