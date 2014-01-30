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
        <%@ include file="/includes/header.jsp" %>
        <title>Home</title>
    </head>
    <body>
        <% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
        <div class="navbar mynavbar" style="display:none;background-color: white;" >
            <div class="container">
                <div class="row">
                    <div class="col-xs-5 col-md-6 col-lg-5"><a href ="/projectProgWeb2/pages/hm?opcode=index" class="mybrand">StudyTalk</a></div>
                    <div class="col-xs-5 col-md-3 col-lg-3 nav-prof" style="text-align: center" >
                        <a href="/projectProgWeb2/pages/hm?opcode=profile">
                            <img src="<%=user.getAvatar()%>" />  
                            <%= user.getUsername()%>
                        </a>
                    </div>
                    <div class="col-xs-2 col-md-2 col-lg-2" style="margin-top:10px;">
                        <div class="btn btn-success pull-left">
                            <a href="/projectProgWeb2/pages/hm?opcode=creategroup">
                                <span class="glyphicon glyphicon-edit"></span>
                            </a>
                        </div>
                    </div>
                    <div class="col-xs-2 col-md-2 col-lg-2 pull-right" style="margin-top:10px;">
                        <div class="btn btn-default">
                            <a href="/projectProgWeb2/pages/hm?opcode=logout" class="glyphicon glyphicon-log-out"></a>
                        </div>                
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <jsp:include page="/includes/messagedisplayer.jsp" />
            <div class="row">
                <div class="col-xs-10 col-md-3 col-lg-3">&nbsp;</div>
                <div class="col-xs-10 col-md-6 col-lg-6 card" style="text-align: center;">
                    <h4>Hello, <c:out value="${user.username}"/>! </h4>
                    <p>L' ultimo accesso è stato:
                        <strong><%= Support.getDateFromTime(user.getLastLogin())%></strong>
                    </p>
                    <c:choose>
                        <c:when test="${inv.size() > 0}" >
                            <table class="table table-condensed table-hover">
                                <tr>
                                    <td><b>Owner</b></td>
                                    <td><b>Group Name</b></td>
                                    <td><b>Creation Date</b></td>
                                    <td><b>Accept/Decline</b></td>
                                </tr>
                                <c:forEach items="${inv}" var="inv">
                                    <tr>
                                        <td><c:out value="${inv.getNameOwner()}"/></td>
                                        <td><c:out value="${inv.getTitle()}"/></td>
                                        <td><fmt:formatDate pattern="dd-MM-yyyy" value="${inv.getDate()}" /></td>
                                        <td>
                                            <a href="/projectProgWeb2/pages/hm?opcode=acceptinv&gid=<c:out value="${inv.getGroupid()}"/>" class="btn btn-success">Accept</button></a>
                                            <a href="/projectProgWeb2/pages/hm?opcode=acceptinv&gid=<c:out value="${inv.getGroupid()}"/>&dec=1" class="btn btn-danger">Decline</button></a>
                                        </td>
                                    <tr>
                                    </c:forEach>
                            </table>
                        </c:when>
                        <c:otherwise >
                            <p>Non ci sono nuovi inviti</p>
                        </c:otherwise>
                    </c:choose>
                    <p>Non ci sono inviti<br />:(</p>

                </div>
            </div>
            <div class="row">
                <c:forEach items="${groups}" var="group">
                    <div class="col-xs-10 col-md-3 col-lg-3">
                        <div class="card">
                            <a href="/projectProgWeb2/pages/hm?opcode=threadgroup&gid=<c:out value="${group.groupid}" />"></a>
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
                    <script type="text/javascript" src="/projectProgWeb2/js/navbar.js"></script>
    </body>
</html>