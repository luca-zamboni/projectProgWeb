<%-- 
    Document   : creategroup
    Created on : Jan 9, 2014, 8:47:55 PM
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
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file="includes/header.jsp" %>
        <title>Create the new group</title>
    </head>
    <body>
        <jsp:include page="includes/navigationbar.jsp" />
        
        <% UserBean us = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
        <div class="container">
            <div class="row">
                <div class="col-xs-1 col-md-3 col-lg-3">&nbsp;</div>
                <div class="col-xs-10 col-md-6 col-lg-6 card">
                    <jsp:include page="includes/messagedisplayer.jsp" />
                    <form action="groupCreate" method="POST">
                            <input type="text" placeholder="Titolo" name="<%=RequestUtils.GROUP_TITLE%>" style="width:100%" />
                            <p> Privato? <input type="checkbox" name="<%= RequestUtils.GROUP_PRIVATE%>"/> </p>
                            <b>Scegli chi invitare:</b><br>
                            <c:forEach items="${userlist}" var="userb" >
                                <c:if test="${userb.userID!=sessionScope.user.userID}">
                                    <input type="checkbox" name="usercheckboxes" value="<c:out value="${userb.username}"/>"/><c:out value="${userb.username}"/><br>
                                </c:if>
                            </c:forEach><br>
                            <input class="btn btn-success" type="submit" id="regBtn" value="Aggiungi gruppo"/>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
