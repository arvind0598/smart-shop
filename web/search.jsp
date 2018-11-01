<%-- 
    Document   : search
    Created on : 28 Oct, 2018, 6:28:42 PM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    String str = null;
    try {
        str = request.getParameter("search");
        if(str == null) throw new Exception();
        str = str.trim().toLowerCase();
    }
    catch(Exception e) {
        response.sendRedirect("index.jsp");
    }
    JSONObject products = new Project.Process().searchProducts(str);
    request.setAttribute("products", products);
    session.setAttribute("currentpage", "search.jsp?" + request.getQueryString());
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Search | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="main.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div>
            <c:forEach items="${products}" var="product">
                <div>
                    <img src="images/${product.key}.png" style="width:100px"/>
                    <a href="product.jsp?id=${product.key}"> ${product.value.name} </a>
                    <p>
                        <c:if test="${product.value.offer ne 0}">
                            <s> ${product.value.cost} </s>
                        </c:if>
                        <c:out value="${product.value.cost - product.value.offer}"/>
                    </p>      
                </div>
            </c:forEach>
        </div>
    </body>
</html>
