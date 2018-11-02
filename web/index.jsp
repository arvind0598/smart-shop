<%--
    Document   : index
    Created on : 19 Oct, 2018, 11:59:19 AM
    Author     : de-arth
--%>

<%@page import="org.json.simple.JSONArray"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    JSONObject categories = new Project.Process().getCategories();
    request.setAttribute("categories", categories);
    session.setAttribute("currentpage", "index.jsp");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div>
            <c:forEach items="${categories}" var="cat">
                <a href="category.jsp?id=${cat.key}"> ${cat.value} </a>
            </c:forEach>
        </div>
    </body>
</html>