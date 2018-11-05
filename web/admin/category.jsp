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
    JSONObject products = new Project.Process().getProductsForAdmin(cat_id);
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
                        <th> Cost </th>
                        <th> Offer </th>
                        <th> Stock </th>
                        <th> Remove Item </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${products}" var="product">
                        <tr id="product${product.key}}">                
                            <td><img src="../images/${product.key}.png" style="height:40px"></td>
                            <td><p> ${product.value.name} </p></td>
                            <td><p> ${product.value.cost} </p></td> 
                            <td>
                                <form class="container addoffer">
                                    <div class="input-field inline">
                                        <input type="text" name="offer" class="validate" value="${product.value.offer}" required data-id="${product.key}">
                                    </div>
                                </form>
                            </td>
                            <td>
                                <form class="container addstock">
                                    <div class="input-field inline">
                                        <input type="text" name="stock" class="validate" value="${product.value.stock}" required data-id="${product.key}">
                                    </div>
                                </form>
                            </td>
                            <td>
                                <button class="btn-floating btn-large waves-effect waves-light red" onclick="deleteProduct(${product.key})">
                                    <i class="material-icons right">delete</i>
                                </button>
                            </td>
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
            const stocks = $(".addstock");
            const offers = $(".addoffer");

            const submitUpdate = (type, value, id) => {
                $.ajax({
                    type: "POST",
                    url: "../serve_modstockoffer",
                    data: {
                        type: type,
                        value: value,
                        id: id
                    },
                    success: data => {
                        console.log(data);
                        M.toast({
                            html: data.message
                        });
                    },
                    error: err => {
                        M.toast({
                            html: "There has been a server error. Please try again."
                        });
                        console.log(err);
                    }
                });
            }
            
            const deleteProduct = id => {
                $.ajax({
                    type: "POST",
                    url: "../serve_rmvitem",
                    data: {
                        id: id
                    },
                    success: data => {
                        console.log(data);
                        M.toast({
                            html: data.message,
                            completeCallback: window.location.reload(true)
                        });
                    },
                    error: err => {
                        M.toast({
                            html: "There has been a server error. Please try again."
                        });
                        console.log(err);
                    }
                });
            }

            stocks.on("submit", event => {
                event.preventDefault();
                event.stopPropagation();
                console.log(event.currentTarget[0].dataset.id);
                submitUpdate(0, event.currentTarget[0].value, event.currentTarget[0].dataset.id);
                return false;
            });

            offers.on("submit", event => {
                event.preventDefault();
                event.stopPropagation();
                submitUpdate(1, event.currentTarget[0].value, event.currentTarget[0].dataset.id);
                return false;
            });
        </script>

    </body> 
</html>
