<%-- 
    Document   : index
    Created on : 19 Oct, 2018, 11:59:19 AM
    Author     : de-arth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

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
        <div> Project is now running for <%=session.getAttribute("trial")%></div>
        <c:forEach var="i" begin="1" end="10">
            <p><c:out value="${i}"/></p>
        </c:forEach>
    </body>
</html>