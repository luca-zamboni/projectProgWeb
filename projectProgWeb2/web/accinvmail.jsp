<%-- 
    Document   : accinvmail
    Created on : 1-feb-2014, 21.22.16
    Author     : luca
--%>

<%@page import="utils.RequestUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="container" style="text-align:center">
            <jsp:include page="includes/messagedisplayer.jsp" />
            <h1>Login prima di accettare un invito</h1>
            <form  action="acceptinvitation" method="POST">
                <input placeholder=username type=text name="<%=RequestUtils.USERNAME%>" />
                <input placeholder=password type=password name="<%=RequestUtils.PASSWD%>" />
                <input type="hidden" name="gid" value="${param["gid"]}" />
                <input class="btn btn-primary" type="submit" value="Entra!" />
                <a class="btn btn-warning" href="forgetPassword.jsp">Password dimenticata?</a>
            </form>
        </div>
    </body>
</html>
