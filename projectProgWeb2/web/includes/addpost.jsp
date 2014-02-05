<%-- 
    Document   : addpost.jsp
    Created on : 23-gen-2014, 21.16.25
    Author     : luca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
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
