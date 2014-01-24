<%-- 
    Document   : modifygroup
    Created on : 22-gen-2014, 21.54.23
    Author     : forna
--%>

<%@page import="utils.SessionUtils"%>
<%@page import="utils.Support"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.UserBean"%>
<%@page import="utils.RequestUtils"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create the new group</title>
    </head>
    <body>
        <jsp:include page="includes/messagedisplayer.jsp" />
        <% UserBean us = (UserBean) Support.getInSession(request, SessionUtils.USER); %>
        <a href="home.jsp">home</a>
        <h1>Modify the group <c:out value="${group.name}"/></h1><br>
        <form action="groupCreate" method="POST" >
            <p>
                Private group?: <% out.println("<input type=\"checkbox\" name=\"" + RequestUtils.GROUP_PRIVATE + "\"/>"); %><br>
                Title of the group: <% out.println("<input type=\"text\" name=\"" + RequestUtils.GROUP_TITLE + "\"/>");%><br>
                <b>Scegli chi invitare:</b><br>
                <c:forEach items="${userlist}" var="userb" >
                    <c:if test="${userb.userID!=sessionScope.user.userID}">
                        <input type="checkbox" name="usercheckboxes" value="<c:out value="${userb.userID}"/>"/><c:out value="${userb.username}"/><br>
                    </c:if>
                </c:forEach>
                <input type="submit" id="regBtn" value="Aggiungi gruppo"/>
            </p>
        </form>
    </body>
</html>
