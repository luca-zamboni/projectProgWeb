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
                <div class="col-xs-12 col-md-3 col-lg-3" style="text-align: center;">
                    <div class="card">
                        <h4>Hello, <c:out value="${user.username}"/>! </h4>
                        <p>L' ultimo accesso è stato:
                            <strong><%= Support.getDateFromTime(user.getLastLogin())%></strong>
                        </p>
                    </div>
                </div>
                <c:forEach items="${invites}" var="inv">
                    <div class="col-xs-12 col-md-3 col-lg-3">
                        <div class="card">
                            <strong><c:out value="${inv.getTitle()}"/></strong>
                            <c:choose>
                                <c:when test="${inv.priva}" >
                                    <span class="label label-danger pull-right">Privato
                                    </c:when>
                                    <c:otherwise >
                                        <span class="label label-primary pull-right">Publico
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <p class="text-muted"><c:out value="${inv.getNameOwner()}"/></p>

                                <p class="text-muted"><fmt:formatDate pattern="dd-MM-yyyy" value="${inv.getDate()}" /></p>
                                <div style="text-align:center">
                                    <a href="./acceptinvitation?gid=<c:out value="${inv.getGroupid()}"/>" class="inv btn btn-success">Unisciti!</a>
                                    <a href="./acceptinvitation?gid=<c:out value="${inv.getGroupid()}"/>&dec=1" class="inv btn btn-danger">Rifiuta!</a>
                                </div>
                        </div>
                    </div>

                </c:forEach>

                <c:forEach items="${groups}" var="group">
                    <div class="col-xs-12 col-md-3 col-lg-3">
                        <div class="card">
                            <a href="threadgroup?gid=<c:out value="${group.groupid}" />"></a>
                            <c:if test="${userID==group.owner}" >
                                <a class="mod glyphicon glyphicon-edit" href="modgroup?gid=<c:out value="${group.groupid}" />"></a>
                            </c:if>
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
                                <p style="text-align:center;">
                                    <img src="img/img.jpg" class="prof" >
                                    <span class="badge" style="position:relative;left:-5%;top:-20%"><
                                        c:out value="${group.numPartecipanti}" />
                                    </span>
                                    </img>
                                </p>
                                <p>Numero di post: <c:out value="${group.numPost}"/></p>
                                <p style="margin-top:3px;">
                                    Ultima risposta: 
                                    <fmt:formatDate pattern="dd-MM-yyyy hh:mm" value="${group.lastPostDate}" />
                                </p>
                        </div>
                    </div>
                </c:forEach> 
            </div>
        </div>
    </div>
    <script type="text/javascript" src="js/navbar.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
           $(".card").hover(function(){
             $(this).children(".mod").fadeToggle();  
           });
        });
    </script>
</body>
</html>
