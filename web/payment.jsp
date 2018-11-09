<%-- 
    Document   : payment
    Created on : 27 Oct, 2018, 10:02:18 PM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);

    Integer customer_id = (Integer) session.getAttribute("login");
    Long total_cost = (Long) session.getAttribute("totalCost");
    Long total_savings = (Long) session.getAttribute("totalSavings");

    if (customer_id == null || customer_id < 1) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Payment | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>  
        <style>            
            #loader {
                position: absolute;
                left: 0;
                top: 0;
                transition: all 500ms ease;
                z-index: 10;
            }

            .loading {
                background: rgba(255,255,255,0.8);
                height: 100%;
                width: 100%;
            }
        </style>
    </head>
    <body>
        <div id="loader"></div>
        <%@ include file="navbar.jspf"%>
        <table>
            <tr>
                <th> Image </th>
                <th> Product Name </th>
                <th> Cost </th>
                <th> Quantity </th>
                <th> Sub Total Cost </th>
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
                    <c:set var="totalCost" value="${sessionScope.totalCost + subCost}" scope="session"/>
                    <c:set var="totalSavings" value="${sessionScope.totalSavings + product.value.offer * product.value.qty}" scope="session"/>
                    <td> 
                        ${subCost} 
                    </td>
                </tr>
            </c:forEach>    
        </table>
        <p><b> Total Effective Cost:</b> <span id="total">${sessionScope.totalCost}</span></p>
        <fmt:parseNumber var="maxPoints" value="${sessionScope.totalCost/2}" type="number" pattern="#" />
        <fmt:parseNumber var="currPoints" value="${sessionScope.details.points}" type="number" pattern="#"/>
        <c:set value="${(maxPoints < currPoints) ? maxPoints : currPoints}" var="allowedPoints" scope="session"/> 
        <p>Available Points: ${sessionScope.allowedPoints}</p>
        <label>
	 <input type="checkbox" id="use_points" name="points_taken" value="1" onclick="usePoints()">
	 <span>Use Points</span>
	</label>
        <p><b> Total Savings:</b> <span id="savings">${sessionScope.totalSavings}</span></p>

        <button onclick="loader('PayTM')" class="waves-effect waves-light btn"> Pay via PayTM </button>
        <button onclick="loader('Tez')" class="waves-effect waves-light btn"> Pay via Tez </button>
        <button onclick="loader('Online Banking')" class="waves-effect waves-light btn"> Pay via Online Banking </button>
    </body>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="js/materialize.min.js"></script>

    <script>

            let totalWithoutPoints = ${sessionScope.totalCost};
            let totalSavings = ${sessionScope.totalSavings};
            let points = ${sessionScope.allowedPoints};

            let checkbox = $("#use_points");
            let spanTotal = $("#total");
            let spanSavings = $("#savings");

            const usePoints = () => {
                if (checkbox.prop("checked")) {
                    spanTotal.text(totalWithoutPoints - points);
                    spanSavings.text(totalSavings + points);
                } else {
                    spanTotal.text(totalWithoutPoints);
                    spanSavings.text(totalSavings);
                }
            }

            const loader = type => {
                alert("Simulating " + type + " payment.");
                $("#loader").addClass("loading");
                $.ajax({
                    type: "POST",
                    url: "serve_checkout",
                    data: {
                        "points": checkbox.prop("checked")
                    },
                    success: data => {
                        console.log(data);
                        window.setTimeout(() => {
                            $("#loader").removeClass("loading");
                            M.toast({
                                html: "Order succesfully placed. Please check your inbox for details.",
                                displayLength: 2500,
                                completeCallback: function() {
                                    window.location.href = "index.jsp";
                                }
                            });
                        }, 3000);
                    },
                    error: err => {
                        console.log(err);
                        alert("There has been a server error. Please try again.");
                    }
                });

            }


    </script>    

</html>
