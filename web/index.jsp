<%-- 
    Document   : index
    Created on : 19 Oct, 2018, 11:59:19 AM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<% 
    JSONObject categories = new Test.Process().getCategories();
    request.setAttribute("categories", categories);
    session.setAttribute("currentpage", "index.jsp");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="main.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div> Project is now running for <%=session.getAttribute("login")%></div>
        <div>
            <c:forEach items="${categories}" var="cat">
                <a href="category.jsp?id=${cat.key}"> ${cat.value} </a>
            </c:forEach>
        </div>
    </body>
</html>