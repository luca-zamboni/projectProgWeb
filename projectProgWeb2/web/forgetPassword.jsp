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
        <title>Password dimenaticata?</title>
    </head>
    <body>
        <h1>Insert your username or email</h1>
         <p><%= Support.getMessageFromSession(request.getSession()) %></p>
        <form action="ForgetPassword" method="POST">
            <input type="text" name="mail" />
        </form>
    </body>
</html>
