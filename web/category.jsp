<%-- 
    Document   : category
    Created on : 24 Oct, 2018, 7:39:10 PM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    Integer cat_id = null;
    try {
        cat_id = new Integer(request.getParameter("id"));
        if(cat_id < 1) throw new Exception();
    }
    catch(Exception e) {
        response.sendRedirect("index.jsp");
    }
    JSONObject products = new Test.Process().getProducts(cat_id);
    request.setAttribute("products", products);
    session.setAttribute("currentpage", "category.jsp?" + request.getQueryString());
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Categories | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="main.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <h1>Hello World!</h1>
        <div>
            <c:forEach items="${products}" var="product">
                <div>
                    <img src="images/${product.key}.png"/>
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
