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
    request.setAttribute("product", product);
    request.setAttribute("product_id", item_id);
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
        <div id="product"></div>
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
            <button id="add"> Add To Cart </button>
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
                        message.text(data.message);
                    },
                    error : err => {
                        console.log(err);
                        message.text("There has been a server error. Please try again.");
                    } 
                });
            });
        </script>
    </body>
</html>
