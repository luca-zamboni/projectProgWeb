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
        <% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER); %>
        <p><%= user.toString() %></p>
        <form action="modprofile" enctype="multipart/form-data" method="POST">
            <% out.println("<input name=\""+RequestUtils.PARAM+"\""
                    + " type=\"hidden\" value=\"" + RequestUtils.AVATARMOD+"\"/>"); %>
            <% out.println("<input required type=\"file\" name=" + RequestUtils.AVATAR + " accept=\"image/*\" />");%>
            <input type="submit" value="change..." disabled />
        </form> 
    </body>
    <script type="text/javascript" src="js/avatar.js" ></script>
</html>
