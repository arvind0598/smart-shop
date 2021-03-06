<%-- 
    Document   : index
    Created on : 1 Nov, 2018, 9:52:21 PM
    Author     : de-arth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);

    Integer x = (Integer) session.getAttribute("admlogin");
    if (x != null) {
        response.sendRedirect("landing.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Login | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="../css/materialize.min.css"  media="screen,projection"/>  
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <form id="login" class="container row valign-wrapper">
            <div class="input-field col s6">
                <input id="user" type="email" name="useremail" class="validate" required/>
                <label for="user"> E-mail ID </label>
            </div>
            <div class="input-field col s6">
                <input id="pass" type="password" name="password" class="validate" required/>
                <label for="pass"> Password </label>
            </div>
            <button class="btn waves-effect waves-light" type="submit">Submit
                <i class="material-icons right">send</i>
            </button>
        </form>

        <div class="slider">
            <ul class="slides">
                <li>
                    <img src="https://static1.squarespace.com/static/537c12a7e4b0ca838c2ee48d/564c0158e4b06f49e6635c6a/5652add8e4b02e2458acf0d1/1460365453183/Gateway_1200+Blur.jpg?format=2500w">
                    <div class="caption center-align">
                        <h3>WELCOME ADMIN</h3>
                        <h5 class="light grey-text text-lighten-3">Efficient Way Of Keeping Track Of Your Stocks</h5>
                    </div>
                </li>
            </ul>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="../js/materialize.min.js"></script>

        <script>
            let form = $("#login");
            let message = $("#message");
            form.on("submit", event => {
                event.preventDefault();
                event.stopPropagation();
                $.ajax({
                    type: "POST",
                    url: "../serve_admlogin",
                    data: form.serializeArray(),
                    success: data => {
                        if (data.status > 0) {
                            window.location.href = "landing.jsp";
                        }
                        M.toast({html: data.message, displayLength: 1000});
                    },
                    error: err => {
                        M.toast({html: "There has been a server error. Please try again."});
                        console.log(err);
                    }
                });
                return false;
            });
            $(document).ready(function () {
                $('.slider').slider();
            });

        </script>
    </body>
</html>
