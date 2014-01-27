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
            <table id="tablegroups">
                <thead>
                    <tr>
                        <th>Group name</th>
                        <th>Owner</th>
                        <th>Creation date</th>
                        <th>Admin</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${groups.size()>0}">
                            <c:forEach var="gr" items="${groups}"> 
                                 <tr>
                                    <td><c:out value="${gr.getTitle()}" /></td>
                                    <td><c:out value="${gr.getNameOwner()}" /></td>
                                    <td><fmt:formatDate pattern="dd-MM-yyyy" value="${gr.getDate()}" /></td>
                                    <td>E tu vorresti amministare?</td>
                                 </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                                <tr><td> No groups </td></tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </body>
</html>
