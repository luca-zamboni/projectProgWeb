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
        <jsp:include page="includes/navigationbar.jsp" />
        <div class="container">
            <jsp:include page="includes/messagedisplayer.jsp" />
            <div class="row">
                <div class="col-xs-1 col-md-1 col-lg-1">&nbsp;</div>
                <div class="col-xs-12 col-md-7 col-lg-7" style="text-align: center;">
                    <div class="card">
                        <h4>Imagine profilo</h4>
                        <img src="<%=user.getAvatar()%>" />
                        <form action="modprofile" enctype="multipart/form-data" method="POST">
                            <input name="<%= RequestUtils.PARAM%>" type="hidden" value="<%=RequestUtils.AVATARMOD%>" />
                            <input required type="file" name="<%= RequestUtils.AVATAR%>" accept="image/*" style="width:100%" />
                            <input style="display: inline;margin-top:5px;" id="avatarBtn" class="btn btn-success" type="submit" value="Aggiorna!" disabled />
                        </form>
                    </div>
                </div>
                <div class="col-xs-12 col-md-4 col-lg-4" style="text-align: center;">
                    <div class="card">

                        <h4>Cambio password</h4><br/>
                        <form action="modprofile"  method="POST">
                            <input name="<%=RequestUtils.PARAM%>" type="hidden" value="<%=RequestUtils.PASSWORDMOD%>"/>
                            <input required placeholder="nuova password" type="password" pattern=".{6,}" title="Almeno 6 caratteri" name="<%=RequestUtils.PASSWD%>" />
                            <br/><input style="margin-top:5px;" type="submit" class="btn btn-danger" value="Esegui!" />
                        </form> 
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript" src="js/avatar.js" ></script>
</html>
