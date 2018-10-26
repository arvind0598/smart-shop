<%-- 
    Document   : checkout
    Created on : 27 Oct, 2018, 12:08:53 AM
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
    request.setAttribute("products", products);
    request.setAttribute("details", details);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Checkout | S Mart</title>
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
            </tr>
            <c:set var="totalCost" value="${0}"/>
            <c:set var="totalSavings" value="${0}"/>
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
                    <c:set var="totalCost" value="${totalCost + subCost}"/>
                    <c:set var="totalSavings" value="${totalSavings + product.value.offer * product.value.qty}"/>
                    <td> 
                        ${subCost} 
                    </td>
                </tr>
            </c:forEach>    
        </table>
            
        <fmt:formatNumber var="maxPoints" value="${totalCost/2}" type="number" pattern="#" />
        <fmt:formatNumber var="currPoints" value="${details.points}" type="number" pattern="#"/>
        <c:set value="${maxPoints > currPoints ? currPoints: maxPoints}" var="allowedPoints"/>
            
        <p><b> Total Effective Cost:</b> <span id="total">${totalCost}</span></p>
        <p>Do you want to apply ${allowedPoints} points? </p>
        <input type="checkbox" id="use_points" name="points_taken" value="1" onclick="usePoints()"> 
        <p><b> Total Savings:</b> <span id="savings">${totalSavings}</span></p>
        
        <button onclick="confirmOrder()"> Confirm Order </button>
    </body>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
    </script>

    <script>
        let totalWithoutPoints = ${totalCost};
        let totalSavings = ${totalSavings};
        let points = ${allowedPoints};
        
        let checkbox = $("#use_points");
        let spanTotal = $("#total");
        let spanSavings = $("#savings");
        
        const usePoints = () => {
            console.log("aa");
            console.log(checkbox.prop("checked"));
            if(checkbox.prop("checked")) {
                spanTotal.text(totalWithoutPoints - points);
                spanSavings.text(totalSavings + points);
            }
            else {
                spanTotal.text(totalWithoutPoints);
                spanSavings.text(totalSavings);
            }
        }
        
        
    </script>
</html>
