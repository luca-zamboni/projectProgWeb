<%-- 
    Document   : cambiopassword
    Created on : 5-gen-2014, 16.30.40
    Author     : luca
--%>

<%@page import="utils.Support"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file="includes/header.jsp" %>
        <title>Forgot password?</title>
        <style type="text/css">
            body{
                background: #222 url('img/login_back.jpg') no-repeat;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-1 col-md-2 col-lg-3">&nbsp;</div>
                <div class="col-xs-10 col-md-8 col-lg-6" style="margin-top:40px;">
                    <div class="bubble" style="display:none;margin-top:40px;">
                        <p>Non preoccuparti, succede...</p>
                        <jsp:include page="includes/messagedisplayer.jsp" />
                        <form action="ForgetPassword" method="POST">
                            <input type="text" name="mail" placeholder="@mail or username" />
                            <input style="padding:10px;font-size: 14px;" class="btn btn-success" type="submit" value="Reset" />
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript" >
        $(document).ready(function(){
           $(".bubble").fadeIn('slow'); 
        });
    </script>
</html>
