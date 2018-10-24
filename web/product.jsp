<%-- 
    Document   : product
    Created on : 24 Oct, 2018, 11:34:28 PM
    Author     : de-arth
--%>

<%@page import="org.json.simple.JSONObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
    Integer item_id = null;
    try {
        item_id = new Integer(request.getParameter("id"));
        if(item_id < 1) throw new Exception();
    }
    catch(Exception e) {
        response.sendRedirect("index.jsp");
    }
    
    JSONObject x = new Test.Process().getProductDetails(item_id);
%>

<!DOCTYPE html>
<html>
    <head>
        <title><%=x.get("name")%> | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="main.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div id="product"></div>
        <c:if test="${sessionScope.login ne null}">
            <button id="add"> Add To Cart </button>
        </c:if>
        <p id="message"></p>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
        </script>
        
        <script>
            const getTemplate = (id, json) => {
                let link = name.toLowerCase().split(" ").join("");                
                let tagImg = $("<img/>").attr("src", "images/" + id + ".png");
                let tagName = $("<p></p>").text(json.name).attr("href", "product.jsp?id=" + id);
                let tagDetails = $("<p></p>").text(json.details)
                let costString = (json.offer ? "<s>" + json.cost + "</s> " : "") + (json.cost - json.offer);
                console.log(costString);
                let tagCost = $("<p></p>").html(costString);
                let div = $("<div></div>").append(tagImg).append(tagName).append(tagCost).append(tagDetails);
                return div;
            }
            
            let message = $("#message");            
            let productDiv = $("#product");
            let product = <%=x.toString()%>;
            let id = <%=item_id%>;
            console.log(product);
            productDiv.append(getTemplate(id, product)); 
            
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
//                        message.text("There has been a server error. Please try again.");
                        message.html(err);
                        console.log(err);
                    }
                });
            });
            
        </script>
    </body>
</html>
