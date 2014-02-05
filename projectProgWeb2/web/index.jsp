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
                $(".bubble").slideDown();
            });
        </script>
    </head>
    <body>
        <div class="navbar mynavbar" >
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-md-2 col-lg-2"><p class="mybrand">StudyTalk</p></div>
                    <div class="col-xs-12 col-md-10 col-lg-10">
                        <jsp:include page="includes/messagedisplayer.jsp" />
                        <form  action="login" method="POST" class="pull-right">
                            <input placeholder=username type=text name="<%=RequestUtils.USERNAME%>" />
                            <input placeholder=password type=password name="<%=RequestUtils.PASSWD%>" />
                            <input class="btn btn-primary" type="submit" value="Entra!" />
                            <a class="btn btn-warning" href="ForgetPassword">Password dimenticata?</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-xs-1 col-md-2 col-lg-6">&nbsp;</div>
                <div class="col-xs-10 col-md-8 col-lg-6" style="margin-top:40px;">
                    <div class="bubble" style="display: none;">
                        <p>
                            Il posto <strong>perfetto</strong> per discutere 
                            dei tuoi progetti,<strong> Idee</strong>, e compiti. 
                        </p>
                        <p>
                            puoi anche condividere <strong>appunti</strong>, immagini,
                            o <strong>registrazioni</strong>. 
                        </p>
                        <p>Per studiare sempre al 
                            <strong>meglio</strong></p><br/>
                        <p>
                            <a class="btn btn-success" style="padding:15px;font-size: 25px;" href="register">Iscriviti adesso!</a>
                            <a class="btn btn-warning" style="padding:15px;font-size: 25px;" href="home">Naviga</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

