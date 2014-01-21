<%-- 
    Document   : messagedisplayer
    Created on : Jan 8, 2014, 8:57:42 PM
    Author     : jibbo
--%>

<%@page import="beans.Message.MessageType"%>
<%@page import="beans.Message"%>
<%@page import="utils.Support"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<p 
<%
    Message msg = Support.getMessageInRequest(request);
    if(msg!=null){
        if(msg.getType()==MessageType.ERROR){ 
%>
            class="btn-danger" style="padding: 5px; border-radius: 3px;">
<%      }
        else{
%>        
            class="btn-success">
<%
        }
        out.print(msg.toString());
    }
%>
</p>
