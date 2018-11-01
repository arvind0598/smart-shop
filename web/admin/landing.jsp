<%-- 
    Document   : landing
    Created on : 2 Nov, 2018, 12:17:53 AM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
    
    Integer x = (Integer)session.getAttribute("admlogin");
    if(x == null || x < 1) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    JSONObject categories = new Project.Process().getCategories();
    request.setAttribute("categories", categories);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Admin Home | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <h1>Categories</h1>
        <div>
            <c:forEach items="${categories}" var="cat">
                <div>
                    <a href="category.jsp?id=${cat.key}"> ${cat.value} </a>
                    <button data-catid="${cat.key}"> Remove Category </button>
                </div>
            </c:forEach>
        </div>
        <h4> Add Category </h4>
        <form id="addcat">
            <input type="text" name="cat" required>
            <input type="submit" value="Add Category">
        </form>
    </body>
</html>
