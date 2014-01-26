<%-- 
    Document   : profile.jsp
    Created on : Jan 8, 2014, 10:40:59 PM
    Author     : jibbo
--%>

<%@page import="utils.RequestUtils"%>
<%@page import="beans.UserBean"%>
<%@page import="utils.Support"%>
<%@page import="utils.SessionUtils"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file="includes/header.jsp" %>
        <title>Profile</title>
    </head>
    <body>
        <% UserBean user = (UserBean) Support.getInSession(request, SessionUtils.USER);%>
        <div class="navbar mynavbar" style="background-color: white;" >
            <div class="container">
                <div class="row">
                    <div class="col-xs-8 col-md-10 col-lg-10"><a href ="home.jsp" class="mybrand">StudyTalk</a></div>
                    <div class="col-xs-2 col-md-2 col-lg-2" style="margin-top:10px;">
                        <div class="btn btn-default">
                            <a href="/ProjectProgWeb2/pages/home?opcode=index" class="glyphicon glyphicon-home"></a>
                        </div>
                        <div class="btn btn-default">
                            <a href="/ProjectProgWeb2/pages/log?opcode=logout" class="glyphicon glyphicon-log-out"></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <jsp:include page="/ProjectProgWeb2/includes/messagedisplayer.jsp" />
            <div class="row">
                <div class="col-xs-1 col-md-1 col-lg-1">&nbsp;</div>
                <div class="col-xs-10 col-md-6 col-lg-6 card" style="text-align: center;">
                    <h4>Imagine profilo</h4>
                    <img src="<%=user.getAvatar()%>" />
                    <form action="modprofile" enctype="multipart/form-data" method="POST">
                        <input name="<%= RequestUtils.PARAM%>" type="hidden" value="<%=RequestUtils.AVATARMOD%>" />
                        <input style="display: inline" required type="file" name="<%= RequestUtils.AVATAR%>" accept="image/*" />
                        <input style="display: inline" id="avatarBtn" type="submit" value="Aggiorna!" disabled />
                    </form>
                </div>
                <div class="col-xs-10 col-md-4 col-lg-4 card" style="text-align: center;">
                    <h4>Cambio password</h4><br/>
                    <form action="modprofile"  method="POST">
                            <input name="<%=RequestUtils.PARAM%>" type="hidden" value="<%=RequestUtils.PASSWORDMOD%>"/>
                            <input required placeholder="nuova password" type="password" pattern=".{6,}" title="Almeno 6 caratteri" name="<%=RequestUtils.PASSWD%>" />
                            <input type="submit" value="Esegui!" />
                    </form> 
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript" src="js/avatar.js" ></script>
</html>
