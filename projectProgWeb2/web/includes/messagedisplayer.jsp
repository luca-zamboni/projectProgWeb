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
<%
    Message msg = Support.getMessageInRequest(request);
    if(msg!=null){
        if(msg.getType()==MessageType.ERROR){ 
%>
            <p class="btn-danger">
<%      }
        else{
%>        
            <p class="btn-success">
<%
        }
        out.print(msg.toString());
    }
%>
</p>