<%-- 
    Document   : index
    Created on : Dec 30, 2013, 8:23:28 PM
--%>

<%@page import="beans.Message"%>
<%@page import="beans.UserBean" %>
<%@page import="utils.RequestUtils"%>
<%@page import="utils.SessionUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>



<!DOCTYPE html>
<html>
    <head>  
        <title>Login</title>
        <%@ include file="includes/header.jsp" %>
        <style type="text/css">
            body{
                background: #222 url('img/login_back.jpg') no-repeat;
            }
            p,a{
                font-weight: bold;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $(".bubble").fadeIn();
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-3 col-md-3 col-lg-3">&nbsp;</div>
                <div class="col-xs-6 col-md-6 col-lg-6" style="margin-top:40px;">
                    <div class="bubble" style="display: none;">
                        <p><a class="btn btn-warning" href="register.jsp">Registrati</a></p>
                        <p>--Oppure--</p>
                        <jsp:include page="includes/messagedisplayer.jsp" />
                        <form  action="login" method="POST">
                            <p><input placeholder=username type=text name="<%=RequestUtils.USERNAME%>" /></p>
                            <p><input placeholder=password type=password name="<%=RequestUtils.PASSWD%>" /></p>
                            
                            <p><input class="btn btn-primary" type="submit" value="Entra!" /></p>
                            <p><a class="btn btn-success" href="forgetPassword.jsp">Password dimenticata?</a></p
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

