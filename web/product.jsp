<%-- 
    Document   : product
    Created on : 24 Oct, 2018, 11:34:28 PM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%
    Integer item_id = null;
    try {
        item_id = new Integer(request.getParameter("id"));
        if(item_id < 1) throw new Exception();
    }
    catch(Exception e) {
        response.sendRedirect("index.jsp");
    }
    
    JSONObject product = new Test.Process().getProductDetails(item_id);
    Boolean inCart = false;
    if(session.getAttribute("login") != null) {
        inCart = new Test.Process().checkInCart((Integer)session.getAttribute("login"), item_id);
    }
    request.setAttribute("product", product);
    request.setAttribute("product_id", item_id);
    request.setAttribute("in_cart", inCart);
    session.setAttribute("currentpage", "product.jsp?" + request.getQueryString());
%>

<!DOCTYPE html>
<html>
    <head>
        <title><%=product.get("name")%> | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="main.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div>
            <img src="images/${product_id}.png"/>
            <p>
                <c:if test="${product.offer ne 0}">
                    <s> ${product.cost} </s>
                </c:if>
                <c:out value="${product.cost - product.offer}"/>
            </p> 
            <p> ${product.details} </p>
        </div>
        <c:if test="${sessionScope.login ne null}">
            <c:choose>
                <c:when test="${in_cart eq false}">
                    <button id="add"> Add To Cart </button>
                </c:when>
                <c:otherwise>
                    <button id="check"> View Cart </button>
                </c:otherwise>
            </c:choose>            
        </c:if>
        <p id="message"></p>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
        </script>
        
        <script>
            let id = <%=item_id%>;
            
            $("#add").on("click", event => {
                event.preventDefault();
                event.stopPropagation();
                $.ajax({
                    type : "POST",
                    url : "serve_addcart",
                    data : {
                        "id" : id
                    },
                    success : data => {
                        console.log(data);
                        alert(data.message);
                        window.location.reload(true);
                    },
                    error : err => {
                        console.log(err);
                        message.text("There has been a server error. Please try again.");
                    } 
                });
            });
            
            $("#check").on("click", event => {
                window.location = "cart.jsp";
            });
        </script>
    </body>
</html>
