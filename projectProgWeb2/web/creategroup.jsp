<%-- 
    Document   : creategroup
    Created on : Jan 9, 2014, 8:47:55 PM
    Author     : forna
--%>

<%@page import="utils.RequestUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create the new group</title>
    </head>
    <body>
        <h1>Create the new Group</h1><br>
        <form action="GroupCreate" method="POST" >
            <p>
                Private group?: <% out.println("<input type=\"checkbox\" name=\"" + RequestUtils.GROUP_PRIVATE + "\"/>"); %><br>
                Title of the group: <% out.println("<input type=\"text\" name=\"" + RequestUtils.GROUP_TITLE + "\"/>");%><br>
                
                <%-- TODO add user list here --%>
                
                <input type="submit" id="regBtn" value="Registrati"/>
            </p>
        </form>
    </body>
</html>
