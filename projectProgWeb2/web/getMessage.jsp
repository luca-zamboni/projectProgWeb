<%-- 
    Document   : getMessage
    Created on : 5-gen-2014, 17.29.47
    Author     : luca
--%>

<%@page import="utils.RequestUtils"%>
<%@page import="beans.Message"%>

<%  Message msg = (Message) session.getAttribute(RequestUtils.MESSAGE);%>
<p><%= msg == null ? "" : msg.toString()%></p>
<%  session.setAttribute(RequestUtils.MESSAGE, null);%>