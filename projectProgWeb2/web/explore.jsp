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
        <jsp:include page="includes/navigationbar.jsp" />
        <div class="container">
            <jsp:include page="includes/messagedisplayer.jsp" />
            <div class="row">
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
                                        <span class="label label-primary pull-right">Pubblico
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <p style="text-align:center;">
                                    <img src="img/img.jpg" class="prof" >
                                    <span class="badge" style="position:relative;left:-5%;top:-20%">
                                        <c:choose>
                                            <c:when test="${group.priva}" >
                                                <c:out value="${group.numPartecipanti}" />
                                            </c:when>
                                            <c:otherwise >
                                                Tutti
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                    </img>
                                </p>
                                <p>Numero di post: <c:out value="${group.numPost}"/></p>
                                <p style="margin-top:3px;">
                                    <c:choose>
                                        <c:when test="${group.isChiuso()}" >
                                            <span class="label label-warning">Gruppo CHIUSO!</span>
                                        </c:when>
                                        <c:otherwise >
                                            Ultima risposta: 
                                            <strong><fmt:formatDate pattern="dd-MM-yyyy hh:mm" value="${group.lastPostDate}" /></strong>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                        </div>
                    </div>
                </c:forEach> 
            </div>
        </div>
    </div>
    <script type="text/javascript" src="js/navbar.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $(".card").hover(function() {
                $(this).children(".mod").fadeToggle();
            });
        });
    </script>
</body>
</html>
