<%-- 
    Document   : cart
    Created on : 25 Oct, 2018, 10:36:15 PM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
    
    Integer x = (Integer)session.getAttribute("login");
    if(x == null || x < 1) { 
        response.sendRedirect("index.jsp");
        return;
    }
    JSONObject products = new Test.Process().getCartProducts(x);
    JSONObject details = new Test.Process().getCustomerDetails(x);
    Boolean hasAddress = details.get("address") != null;
    session.setAttribute("products", products);
    session.setAttribute("details", details);
    session.setAttribute("has_address", hasAddress);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Cart | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="main.css" rel="stylesheet"/>
        <style>
            table {
                width: 100%;
            }
            
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            
            th, td {
                padding: 15px;
            }
            
            tr:nth-child(even) {
                background-color: #eee;
            }
        </style>     
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <table>
            <tr>
                <th> Image </th>
                <th> Product Name </th>
                <th> Cost </th>
                <th> Quantity </th>
                <th> Sub Total Cost </th>
                <th> Actions </th> 
            </tr>
            <c:set var="totalCost" value="${0}" scope="session"/>
            <c:set var="totalSavings" value="${0}" scope="session"/>
            <c:forEach items="${products}" var="product">
                <tr>
                    <c:set var="effectiveCost" value="${product.value.cost - product.value.offer}"/>
                    <td>
                        <img src="images/${product.key}.png" style="width:20px"/>
                    </td>
                    <td>
                        <a href="product.jsp?id=${product.key}"> ${product.value.name} </a>
                    </td>
                    <td>
                        <c:if test="${product.value.offer ne 0}">
                            <s> ${product.value.cost} </s>
                        </c:if>
                        <c:out value="${effectiveCost}"/>
                    </td>
                    <td> 
                        ${product.value.qty} 
                    </td>
                    <c:set var="subCost" value="${effectiveCost * product.value.qty}"/>
                    <c:set var="totalCost" value="${totalCost + subCost}" scope="session"/>
                    <c:set var="totalSavings" value="${totalSavings + product.value.offer * product.value.qty}" scope="session"/>
                    <td> 
                        ${subCost} 
                    </td>
                    <td>
                        <button class="add" onclick="updateCart(${product.key},${product.value.qty + 1})"> + </button>
                        <button class="remove" onclick="updateCart(${product.key},${product.value.qty - 1})"> - </button>
                    </td>
                </tr>
            </c:forEach>    
        </table>
        <p><b> Total Effective Cost:</b> <span id="total">${sessionScope.totalCost}</span></p>
        
        <c:if test="${totalCost ne 0}">
            <button onclick="checkOut()"> Proceed to Payment </button>
        </c:if>
        <p> Note: To change address, visit your profile. </p>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
        </script>
        
        <script>
            
            const updateCart = (id, qty) => {
                $.ajax({
                    type : "POST",
                    url : "serve_updatecart",
                    data : {
                        "id" : id,
                        "qty" : qty
                    },
                    success : data => {
                        console.log(data);
                        window.location.reload(true);
                    },
                    error : err => {
                        console.log(err);
                        message.text("There has been a server error. Please try again.");
                    } 
                });
            }
            
            const checkOut = () => {
                <c:choose>
                    <c:when test="${sessionScope.has_address}">
                        window.location.href="payment.jsp";
                    </c:when>
                    <c:otherwise>
                        alert("Please enter address");
                        window.location.href="profile.jsp";
                    </c:otherwise>
                </c:choose>
            }
            
        </script>
    </body>
</html>
