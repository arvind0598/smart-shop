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
        if (cat_id == null || cat_id < 1) {
            throw new Exception();
        }
    } catch (Exception e) {
        response.sendRedirect("landing.jsp");
        return;
    }
    JSONObject products = new Project.Process().getProducts(cat_id);
    request.setAttribute("products", products);
    
    String categoryName = new Project.Process().getCategoryName(cat_id);
    request.setAttribute("name", categoryName);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>${name} | S Mart Admin</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
         <link type="text/css" rel="stylesheet" href="../css/materialize.min.css"  media="screen,projection"/>
        <style>
            body {
                display: flex;
                min-height: 100vh;
                flex-direction: column;
            }

            main {
                flex: 1 0 auto;
            }
        </style>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <main>
            <table class="striped centered">
                <thead>
                    <tr> 
                        <th> Image </th>
                        <th> Name </th>
                        <th> Offer </th>
                        <th> Modify Offer </th>
                        <!--<th> Remove Offer </th>-->
                        <th> Remove Item </th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="product">
                    <tr id="product${product.key}}">                
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
                            <form id="addcat" class="container" onsubmit="return addOffer()">
                                <div class="input-field inline">
                                    <input type="text" name="offer" id="cat" class="validate" required>
                                    <!--<label for="cat"> Category Name </label>-->
                                </div>
                            </form>
                        </td>
<!--                        <td>
                            <button class="btn waves-effect waves-light" type="submit" name="action">Add Category
                                <i class="material-icons right">library_add</i>
                            </button>
                        </td>-->
                        <td>
                            <button class="btn-floating btn-large waves-effect waves-light red">
                                <i class="material-icons right">delete</i>
                            </button>
                        </td>
                        <!--<td></td>-->
                    </tr>

                </c:forEach>
                </tbody>
            </table>
        </main>
        
        <footer class="page-footer grey lighten-5">
            <div class="container">
                <div class="row center-align">
                    <a href="newproduct.jsp?cat_id=${param.id}"> Click here to add a new product to ${name} </a>
                </div>
            </div>
        </footer>
        
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="../js/materialize.min.js"></script>
        
        <script>
             M.updateTextFields();
        </script>
            
    </body> 
</html>
