<%-- 
    Document   : groupthread
    Created on : 21-gen-2014, 13.49.21
    Author     : luca
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="includes/header.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create the new group</title>
    </head>
    <body>
        <jsp:include page="includes/navigationbar.jsp" />
        <div class="container">
            <jsp:include page="includes/messagedisplayer.jsp" />
            <h1 style="text-align:center"><c:out escapeXml="true" value="${group.getTitle()}" /></h1>
            <c:if test="${group.isChiuso()}">
                <h3 style="text-align:center; color:red;">QUesto gruppo è stato chiudo dall'amministratore</h3>
            </c:if>
            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                  <div class="panel-heading">
                    <h4 class="panel-title">
                      <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                        All files <span class="badge"> <c:out value="${group.getAllFiles().size()}" /></span>
                      </a>
                    </h4>
                  </div>
                  <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <ul class="list-group">
                            <c:choose>
                                <c:when test="${group.getAllFiles().size()>0}">
                                    <c:forEach var="file" items="${group.getAllFiles()}"> 
                                        <li class="list-group-item"><a href="files/<c:out value="${group.getGroupid()}" />/<c:out value="${file}" />"><c:out value="${file}" /></a></li>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <li class="list-group-item">No files added</li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                  </div>
                </div>
            </div><br>
            <div>
                <c:forEach var="posts" items="${group.getPosts()}">
                    <div class="thcard">
                        <img src="<c:out value="${posts.getAvatar()}"/>" style="width:100px;"  class="img-thumbnail prof" />
                        <b><c:out value="${posts.getUser()}"/></b> at <c:out value="${posts.getDate()}"/> says:<br> <br> 
                        <div class="row">
                            <div class="col-md-6">
                                <c:out value="${posts.getText()}" escapeXml="false"/> 
                            </div>
                            <div class="col-md-3">
                                <c:choose>
                                    <c:when test="${posts.getFiles().size()>0}">
                                      <div class="btn-group">
                                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                                          File Uploaded <span class="badge"><c:out value="${posts.getFiles().size()}" /></span> <span class="caret"></span>
                                        </button>
                                        <ul class="dropdown-menu" role="menu">
                                          <c:forEach var="file" items="${posts.getFiles()}"> 
                                            <li><a href="files/<c:out value="${group.getGroupid()}" />/<c:out value="${file}" />"><c:out value="${file}" /></a></li>
                                        </c:forEach>
                                        </ul>
                                      </div>
                                    </c:when>
                                </c:choose>
                            </div>
                            <div class="col-md-3">
                                <c:forEach var="qr" items="${posts.getQrs()}">
                                    <img src="<c:out value="${qr}"/>" style="width:70px;heigth:150px;" />
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:import url="includes/addpost.jsp" />
            </div>
        </div>
        <br><br><br><br>
    </body>
</html>
