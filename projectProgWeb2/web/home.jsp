<%-- 
    Document   : home
    Created on : Dec 30, 2013, 9:00:44 PM
--%>

<%@page import="utils.Support"%>
<%@page import="beans.Message"%>
<%@page import="beans.UserBean" %>
<%@page import="utils.RequestUtils"%>
<%@page import="utils.SessionUtils"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <title>Home</title>
    </head>
    <body>
        <% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
        <jsp:include page="includes/navigationbar.jsp" />
        <div class="container">
            <jsp:include page="includes/messagedisplayer.jsp" />
            <div class="row">
                <div class="col-xs-10 col-md-2 col-lg-2">&nbsp;</div>
                <div class="col-xs-10 col-md-4 col-lg-4 card" style="text-align: center;">
                    <h4>Hello, <%=user.getUsername()%>! </h4>
                    <p>L' ultimo accesso è stato:
                        <strong><%= Support.getDateFromTime(user.getLastLogin())%></strong>
                    </p>
                    <p>Non ci sono inviti<br />:(</p>

                </div>
                <div class="col-xs-11 col-md-4 col-lg-4 card" style="text-align: center;">
                    <div class="btn btn-success pull-left">
                        <a href="groupCreate">
                            <span class="glyphicon glyphicon-edit"></span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
