<%-- 
    Document   : navigationbar
    Created on : Jan 22, 2014, 10:38:40 PM
    Author     : jibbo
--%>

<%@page import="utils.SessionUtils"%>
<%@page import="beans.UserBean"%>
<%@page import="utils.Support"%>
<% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
<div class="navbar mynavbar" style="background-color: white;" >
    <div class="container">
        <div class="row">
            <div class="col-xs-5 col-md-6 col-lg-8"><p class="mybrand">StudyTalk</p></div>
            <div class="col-xs-6 col-md-3 col-lg-3 nav-prof" style="text-align: center" >
                <a href="profile.jsp">
                    <img src="<%=user.getAvatar()%>" />  
                    <%= user.getUsername()%>
                </a>

            </div>
            <div class="col-xs-1 col-md-1 col-lg-1" style="margin-top:10px;">
                <div class="btn btn-danger">
                    <a href="./logout" class="glyphicon glyphicon-log-out"></a>
                </div>
            </div>
        </div>
    </div>
</div>
