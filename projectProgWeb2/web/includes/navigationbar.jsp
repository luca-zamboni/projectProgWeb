<%-- 
    Document   : navigationbar
    Created on : Jan 22, 2014, 10:38:40 PM
    Author     : jibbo
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="utils.SessionUtils"%>
<%@page import="beans.UserBean"%>
<%@page import="utils.Support"%>
<% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
<div class="navbar mynavbar" style="display:none;background-color: white;" >
    <div class="container">
        <div class="row">
            <div class="col-xs-5 col-md-6 col-lg-7"><a href ="./home" class="mybrand">StudyTalk</a></div>
            <c:if test="${user != null}">
                <div class="col-xs-5 col-md-3 col-lg-3 nav-prof" style="text-align: center" >
                    <a href="profile.jsp">
                        <img src="<%=user.getAvatar()%>" />  
                        <%= user.getUsername()%>
                    </a>
                </div>
            </c:if>

<<<<<<< HEAD
            <div class="col-xs-2 col-md-2 col-lg-2" style="margin-top:10px;">
                <div class="btn btn-default">
                    <a href="./home" class="glyphicon glyphicon-home"></a>
=======
                <div class="col-xs-2 col-md-2 col-lg-2" style="margin-top:10px;">
                    <a href="./home"><div class="btn btn-default">
                        <span class="glyphicon glyphicon-home"></span>
                    </div></a>
                    <a href="./logout"><div class="btn btn-default">
                        <span class="glyphicon glyphicon-log-out"></span>
                    </div></a>                
>>>>>>> 95481e018f148d19aaf4b4ba4580de30d58ed518
                </div>
            <c:if test="${user != null}">
                <div class="btn btn-default">
                    <a href="./logout" class="glyphicon glyphicon-log-out"></a>
                </div>    
            </c:if>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="js/navbar.js"></script>
