<%-- 
    Document   : category
    Created on : 4 Nov, 2018, 2:54:32 AM
    Author     : de-arth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    Integer cat_id = null;
    try {
        cat_id = new Integer(request.getParameter("id"));
        if (cat_id < 1) {
            throw new Exception();
        }
    } catch (Exception e) {
        response.sendRedirect("index.jsp");
    }
    JSONObject products = new Project.Process().getProducts(cat_id);
    request.setAttribute("products", products);
    session.setAttribute("currentpage", "category.jsp?" + request.getQueryString());
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Categories | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="../css/materialize.min.css"  media="screen,projection"/>  
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div class="container">
            <table class="striped centered">
                <thead>
                    <tr> 
                        <th> Image </th>
                        <th> Name </th>
                        <th> Offer </th>
                        <th> Modify Offer </th>
                        <!--<th> Remove Item </th>-->
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="product">
                    <tr>                
                        <td><img src="../images/${product.key}.png" style="height:40px"></td>
                        <td><a href="product.jsp?id=${product.key}"> ${product.value.name} </a></td>
                        <td>
                            <c:choose>
                                <c:when test="${product.value.offer ne 0}">
                                    <p> Rs.${product.value.offer} </p>
                                </c:when>
                                <c:otherwise>
                                    <p> No Offer </p>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <form id="addcat" class="container" onsubmit="return addCategory()">
                                <div class="input-field inline">
                                    <input type="text" name="category" id="cat" class="validate" required>
                                    <!--<label for="cat"> Category Name </label>-->
                                </div>
                            </form>
                        </td>
                        <!--<td></td>-->
                    </tr>

                </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
