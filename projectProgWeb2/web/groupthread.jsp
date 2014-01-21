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
        <style type="text/css">
        body{
            background:#fff;
        }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create the new group</title>
    </head>
    <body>
        <div class="container">
            <jsp:include page="includes/messagedisplayer.jsp" />
            <h1 style="text-align:center"><c:out value="${group.getTitle()}"/></h1>
            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                  <div class="panel-heading">
                    <h4 class="panel-title">
                      <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                        All files
                      </a>
                    </h4>
                  </div>
                  <div id="collapseOne" class="panel-collapse collapse">
                    <div class="panel-body">
                        <c:if test="${group.getAllFiles().size() > 0}">
                        <ul class="list-group">
                            <c:forEach var="file" items="${group.getAllFiles()}"> 
                                <li class="list-group-item"><a href="files/<c:out value="${group.getGroupid()}" />/<c:out value="${file}" />"><c:out value="${file}" /></a></li>
                            </c:forEach>
                        </ul>
                        </c:if>
                    </div>
                  </div>
                </div>
            </div><br>
            <div>
                    <c:forEach var="posts" items="${group.getPosts()}">
                        <div class="well">
                            <img src="" style="width:60px;heigth:100px;" class="img-thumbnail" />
                            <b><c:out value="${posts.getUserid()}"/></b> at <c:out value="${posts.getDate()}"/> says:<br> <br> 
                            <div class="row">
                                <div class="col-md-2">
                                    <h5>Files:</h5>
                                    <ul class="list-group">
                                        <c:forEach var="file" items="${posts.getFiles()}"> 
                                            <li class="list-group-item"><a href="files/<c:out value="${group.getGroupid()}" />/<c:out value="${file}" />"><c:out value="${file}" /></a></li>
                                        </c:forEach>
                                    </ul>
                                </div>
                                <div class="col-md-8">
                                    <c:out value="${posts.getText()}"/> 
                                </div>
                                <div class="col-md-2">
                                    
                                </div>
                            </div>
                        </div>
                    </c:forEach>
            </div>
            <div class="well">
                <form enctype="multipart/form-data" action="addpost?gid=<c:out value="${group.getGroupid()}"/>" method="POST">
                    <h3>Di la tua</h3>
                    <div class="form-group">
                        <label for="exampleInputFile">File input</label>
                        <input type="file" name="files" multiple/>
                        <p class="help-block">Insert your files</p>
                    </div>
                    <textarea name="post" class="form-control" rows="6"></textarea><br>
                    <button type="submit" class="btn btn-success btn-lg">Submit</button>
                </form>
            </div>
        </div>
    </body>
</html>
