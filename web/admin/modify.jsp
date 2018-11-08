<%-- 
    Document   : modify
    Created on : 3 Nov, 2018, 11:30:59 PM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);

    Integer x = (Integer) session.getAttribute("admlogin");
    if (x == null || x < 1) {
        response.sendRedirect("index.jsp");
        return;
    }

    JSONObject categories = new Project.Process().getCategories();
    request.setAttribute("categories", categories);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Modify Products | S Mart</title>
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
            <div class="container">
                <ul class="collection with-header">
                    <li class="collection-header"> <h4>Categories</h4> </li>
                    <c:forEach items="${categories}" var="cat">
                        <li class="collection-item">
                            <a href="category.jsp?id=${cat.key}" class="title"> ${cat.value} </a>
                            <a href="javascript:removeCategory(${cat.key})" class="secondary-content">
                                <i class="material-icons red-text"> delete </i>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </main>

        <footer class="page-footer grey lighten-5">
            <div class="container">
                <form id="addcat" class=" row valign-wrapper " onsubmit="return addCategory()">
                    <div class="input-field col l5 offset-l3">
                        <input type="text" name="category" id="cat" class="validate" required>
                        <label for="cat"> Category Name </label>
                    </div>
                    <div class="col l4">
                        <button class="btn waves-effect waves-light light-blue darken-4" type="submit" name="action">Add Category
                            <i class="material-icons right">library_add</i>
                        </button>  
                    </div>
                    <div class="col l2"></div>
                </form>
            </div>
        </footer>
    </body>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="../js/materialize.min.js"></script>


    <script>
                const addCategory = () => {
                    $.ajax({
                        type: "POST",
                        url: "../serve_addcat",
                        data: {
                            "category": $("#cat").val()
                        },
                        success: data => {
                            console.log(data);
                            window.location.reload(true);
                        },
                        error: err => {
                            console.log(err);
                            M.toast({
                                html: "There has been a server error. Please try again."
                            });
                        }
                    });

                    return false;
                }

                const removeCategory = (cat_id) => {
                    $.ajax({
                        type: "POST",
                        url: "../serve_remcat",
                        data: {
                            "category": cat_id
                        },
                        success: data => {
                            console.log(data);
                            window.location.reload(true);
                        },
                        error: err => {
                            console.log(err);
                            M.toast({
                                html: "There has been a server error. Please try again."
                            });
                        }
                    });
                }
    </script>

</html>
