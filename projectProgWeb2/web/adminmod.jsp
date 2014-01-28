<%-- 
    Document   : adminmod
    Created on : 27-gen-2014, 23.15.03
    Author     : luca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin</title>
        <link rel="stylesheet" href="./datatable/css/jquery.dataTables.css" />
        <script type="text/javascript" language="javascript" src="./datatable/js/jquery.dataTables.js">
        </script><script type="text/javascript" charset="utf-8">
            $(document).ready(function() {
                $('#tablegroups').dataTable();
            } );
        </script>
    </head>
    <body>
        <jsp:include page="includes/navigationbar.jsp" />
        <div class="container">
            <h1> Admin page :) </h1>
            <jsp:include page="includes/messagedisplayer.jsp" />
            <table id="tablegroups">
                <thead>
                    <tr>
                        <th>Nome gruppo</th>
                        <th>Owner</th>
                        <th>Tipo</th>
                        <th>Numero partecipanti</th>
                        <th>Numero post</th>
                        <th>Chiudi</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${groups.size()>0}">
                            <c:forEach var="gr" items="${groups}"> 
                                 <tr>
                                    <td>
                                        <a href="threadgroup?gid=<c:out value="${gr.getGroupid()}" />">
                                            <c:out value="${gr.getTitle()}" />
                                        </a>
                                    </td>
                                    <td>
                                        <img src="<c:out value="${gr.getAvatarOwner()}"/>" style="width:60px;heigth:100px;" class="img-thumbnail" />
                                        <c:out value="${gr.getNameOwner()}" />
                                    </td>
                                    <c:choose>
                                        <c:when test="${gr.isPriva()}">
                                            <td><span style="color:red"><strong>Privato</strong></div></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td><span style="color:green"><strong>Publico</strong></div></td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td><c:out value="${gr.getNumPartecipanti()}" /></td>
                                    <td><c:out value="${gr.getNumPost()}" /></td>
                                    <td>
                                        <a href="">
                                            <button type="button" class="btn btn-danger">Chiudi</button>
                                        </a>
                                    </td>
                                 </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                                <td>NO grouppi</td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
        <br><br><br>
    </body>
</html>
