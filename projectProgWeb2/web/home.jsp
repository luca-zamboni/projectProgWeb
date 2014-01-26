<%-- 
    Document   : home
    Created on : Dec 30, 2013, 9:00:44 PM
--%>

<%@page import="utils.Support"%>
<%@page import="beans.Message"%>
<%@page import="beans.UserBean" %>
<%@page import="utils.RequestUtils"%>
<%@page import="utils.SessionUtils"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <title>Home</title>
    </head>
    <body>
        <% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
        <div class="navbar mynavbar" style="display:none;background-color: white;" >
            <div class="container">
                <div class="row">
                    <div class="col-xs-5 col-md-6 col-lg-5"><a href ="./home" class="mybrand">StudyTalk</a></div>
                    <div class="col-xs-5 col-md-3 col-lg-3 nav-prof" style="text-align: center" >
                        <a href="profile.jsp">
                            <img src="<%=user.getAvatar()%>" />  
                            <%= user.getUsername()%>
                        </a>
                    </div>
                    <div class="col-xs-2 col-md-2 col-lg-2" style="margin-top:10px;">
                        <div class="btn btn-success pull-left">
                            <a href="groupCreate">
                                <span class="glyphicon glyphicon-edit"></span>
                            </a>
                        </div>
                    </div>
                    <div class="col-xs-2 col-md-2 col-lg-2 pull-right" style="margin-top:10px;">
                        <div class="btn btn-default">
                            <a href="./logout" class="glyphicon glyphicon-log-out"></a>
                        </div>                
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <jsp:include page="includes/messagedisplayer.jsp" />
            <div class="row">
                <div class="col-xs-10 col-md-3 col-lg-3">&nbsp;</div>
                <div class="col-xs-10 col-md-6 col-lg-6 card" style="text-align: center;">
                    <h4>Hello, <c:out value="${user.username}"/>! </h4>
                    <p>L' ultimo accesso è stato:
                        <strong><%= Support.getDateFromTime(user.getLastLogin())%></strong>
                    </p>
                    <p>Non ci sono inviti<br />:(</p>

                </div>
            </div>
            <div class="row">
                <c:forEach items="${groups}" var="group">
                    <div class="col-xs-10 col-md-3 col-lg-3">
                        <div class="card">
                            <a href="threadgroup?gid=<c:out value="${group.groupid}" />"></a>
                            <strong><c:out value="${group.title}" /></strong>
                            <c:choose>
                                <c:when test="${group.priva}" >
                                    <span class="label label-danger pull-right">Privato
                                    </c:when>
                                    <c:otherwise >
                                        <span class="label label-primary pull-right">Publico
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <p><fmt:formatDate pattern="dd-MM-yyyy" value="${group.date}" /></p>
                        </div>
                    </div>
                </c:forEach> 
            </div>
        </div>
    </div>
    <script type="text/javascript" src="js/navbar.js"></script>
</body>
</html>
