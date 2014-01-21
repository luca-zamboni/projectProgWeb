<%-- 
    Document   : register
    Created on : Dec 30, 2013, 9:43:56 PM
    Author     : jibbo
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
        <title>Registrazione</title>
        <style type="text/css">
            body{
                background: #222 url('img/login_back.jpg') no-repeat;
            }
            
            input{
                display:block;
                margin: 0 auto 10px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-1 col-md-2 col-lg-3">&nbsp;</div>
                <div class="col-xs-10 col-md-8 col-lg-6" style="margin-top:40px;">
                    <div class="bubble" style="margin-top:40px;">
                        <jsp:include page="includes/messagedisplayer.jsp" />
                        <p><strong>Presto</strong> sarai dentro, scrivi qui le tue informazioni</p>
                        <form action="register" method="POST" id="regform">
                            <% out.println("<input placeholder=\"email\" type='email' name=" + RequestUtils.EMAIL + " required />"); %>
                            <% out.println("<input pattern=\".{6,}\" title=\"Almeno 6 caratteri\" placeholder=\"username (Opzionale)\" type=\"text\" name=" + RequestUtils.USERNAME + " />"); %>
                            <% out.println("<input pattern=\".{6,}\" title=\"Almeno 6 caratteri\" placeholder=\"password\" type=\"password\" name=" + RequestUtils.PASSWD + " />");%>
                            <input style="padding:15px;font-size: 25px;" class="btn btn-success" type="submit" id="regBtn" value="Registrati"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>
