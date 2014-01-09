<%-- 
    Document   : profile.jsp
    Created on : Jan 8, 2014, 10:40:59 PM
    Author     : jibbo
--%>

<%@page import="utils.RequestUtils"%>
<%@page import="beans.UserBean"%>
<%@page import="utils.Support"%>
<%@page import="utils.SessionUtils"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file="includes/header.jsp" %>
        <title>Profile</title>
    </head>
    <body>
        <jsp:include page="includes/messagedisplayer.jsp" />
        <% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
        <p><%= user.toString()%></p>
        <p class="btn-danger hidden" id="messages"></p>
        <form action="modprofile" enctype="multipart/form-data" method="POST">
            <fieldset>
                <legend>Nuovo avatar</legend>
                <% out.print("<input name=\"" + RequestUtils.PARAM + "\""
                        + " type=\"hidden\" value=\"" + RequestUtils.AVATARMOD + "\"/>"); %>
                <% out.print("<input required type=\"file\" name=" + RequestUtils.AVATAR + " accept=\"image/*\" />");%>
                <input id="avatarBtn" type="submit" value="Vai!" disabled />
            </fieldset>
        </form>
        <form action="modprofile"  method="POST">
            <fieldset>
                <legend>Cambia Password</legend>
                <% out.print("<input name=\"" + RequestUtils.PARAM + "\""
                        + " type=\"hidden\" value=\"" + RequestUtils.PASSWORDMOD + "\"/>"); %>
                <% out.print("<input required type=\"password\" pattern=\".{6,}\" title=\"Almeno 6 caratteri\" name=" + RequestUtils.PASSWD + " />");%>
                <input type="submit" value="Esegui!" />
            </fieldset>
        </form>     
    </body>
    <script type="text/javascript" src="js/avatar.js" ></script>
</html>
