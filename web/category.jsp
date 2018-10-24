<%-- 
    Document   : category
    Created on : 24 Oct, 2018, 7:39:10 PM
    Author     : de-arth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Integer cat_id = null;
    try {
        cat_id = new Integer(request.getParameter("id"));
        if(cat_id < 1) throw new Exception();
    }
    catch(Exception e) {
        response.sendRedirect("index.jsp");
    }
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
        <div id="products"></div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
        </script>
        
        <script>
            const getTemplate = (id, json) => {
                let link = name.toLowerCase().split(" ").join("");                
                let tagImg = $("<img/>").attr("src", "images/" + id + ".png");
                let tagName = $("<a></a>").text(json.name).attr("href", "product.jsp?id=" + id);
                
                let costString = (json.offer ? "<s>" + json.cost + "</s> " : "") + (json.cost - json.offer);
                console.log(costString);
                let tagCost = $("<p></p>").html(costString);
                let div = $("<div></div>").append(tagImg).append(tagName).append(tagCost);
                return div;
            }
            
            let productDiv = $("#products");
            let products = <%=new Test.Process().getProducts(cat_id).toString()%>;
            console.log(products);
            for(let i in products) {
                productDiv.append(getTemplate(i, products[i]));
            }          
            
        </script>
    </body>
</html>
