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
        <title>Create the new group</title>
    </head>
    <body>
        <jsp:include page="includes/navigationbar.jsp" />
        <div class="container">
            <div class="row">
                <div class="col-xs-1 col-md-3 col-lg-3">&nbsp;</div>
                <div class="col-xs-10 col-md-6 col-lg-6 card">
                    <jsp:include page="includes/messagedisplayer.jsp" />
                    <form action="modgroup" method="POST">
                        <input type="hidden" name="<%=RequestUtils.GROUP_ID%>" value="${group.groupid}" />
                        <p><input type="text" placeholder="Titolo" name="<%=RequestUtils.GROUP_TITLE%>" style="width:100%" 
                                  value="<c:out value="${group.title}"/>"/></p>
                            <c:choose>
                                <c:when test="${group.priva}" >
                                <p>Privato?<input id="ckpriv" type="checkbox" name="<%= RequestUtils.GROUP_PRIVATE%>" checked /></p>
                                </c:when>
                                <c:otherwise >
                                <p>Privato?<input id="ckpriv" type="checkbox" name="<%= RequestUtils.GROUP_PRIVATE%>" /></p>
                                </c:otherwise>
                            </c:choose>
                        <div id="inviti">
                            <p><b>Invita:</b></p>
                            <c:forEach items="${userlist}" var="userb" >
                                <c:if test="${userb.userID!=sessionScope.user.userID}">
                                    <c:choose>
                                        <c:when test="${group.priva}" >
                                            <input type="checkbox" name="usercheckboxes" value="<c:out value="${userb.userID}"/>" checked="">
                                        </c:when>
                                        <c:otherwise >
                                            <input type="checkbox" name="usercheckboxes" value="<c:out value="${userb.userID}"/>">
                                        </c:otherwise>
                                    </c:choose>
                                    <c:out value="${userb.username}"/>
                                    </input>
                                </c:if>
                            </c:forEach><br>
                        </div>
                        <p><br/><input class="btn btn-success pull-right" type="submit" id="regBtn" value="Modifica!"/></p>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript" src="js/groupcheckboxes.js" ></script>
</html>
