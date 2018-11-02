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
        if (item_id < 1) {
            throw new Exception();
        }
    } catch (Exception e) {
        response.sendRedirect("index.jsp");
    }

    JSONObject product = new Project.Process().getProductDetails(item_id);
    Boolean inCart = false;
    if (session.getAttribute("login") != null) {
        inCart = new Project.Process().checkInCart((Integer) session.getAttribute("login"), item_id);
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
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>  
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div class="container">
            <div class="row">
                <div class="col l6">
                    <img class="responsive-img" src="images/${product_id}.png"/>
                </div>
                <div class="col l6">
                    <div class="card small">
                        <div class="card-content">
                            <span class="card-title"> ${product.name} </span>
                            <p> Rs.
                                <c:if test="${product.offer ne 0}">
                                    <s> ${product.cost} </s>
                                </c:if>
                                <c:out value="${product.cost - product.offer}"/>
                            </p> 
                            <p> ${product.details} </p> 
                        </div>
                        <div class="card-action">
                            <c:if test="${sessionScope.login ne null}">
                                <c:choose>
                                    <c:when test="${in_cart eq false}">
                                        <button id="add" class="waves-effect waves-light btn-large light-blue darken-3"> Add To Cart </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button id="check" class="waves-effect waves-light btn-large light-blue darken-3"> View Cart </button>
                                    </c:otherwise>
                                </c:choose>            
                            </c:if>
                            <c:if test="${sessionScope.login eq null}">
                                <p> Login to add items to your cart </p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>

        <script>
            let id = <%=item_id%>;

            $("#add").on("click", event => {
                event.preventDefault();
                event.stopPropagation();
                $.ajax({
                    type: "POST",
                    url: "serve_updatecart",
                    data: {
                        "id": id
                    },
                    success: data => {
                        M.toast({
                            html: data.message,
                            displayLength: 2500,
                            completeCallback: function () {
                                window.location.reload(true);
                            }
                        });
                    },
                    error: err => {
                        console.log(err);
                        M.toast({
                            html: "There has been a server error. Please try again."
                        });
                    }
                });
            });

            $("#check").on("click", event => {
                window.location = "cart.jsp";
            });
        </script>
    </body>
</html>
