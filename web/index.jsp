<%-- 
    Document   : index
    Created on : 19 Oct, 2018, 11:59:19 AM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="main.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div> Project is now running for <%=session.getAttribute("login")%></div>
        <div id="categories"></div>       
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
        </script>
        
        <script>
            const getTemplate = (id, name) => {
                let link = name.toLowerCase().split(" ").join("");
                return $("<a></a>").text(name).attr("href", "category.jsp?id=" + id).attr("data-id", id).addClass("cat-link");
            }
            
            let categoryDiv = $("#categories");
            let categories = <%=new Test.Process().getCategories().toString()%>;
            console.log(categories);
            for(let i in categories) {
                categoryDiv.append(getTemplate(i, categories[i]));
            }          
            
        </script>
    </body>
</html>