<%-- 
    Document   : register_page
    Created on : 20 Oct, 2018, 5:08:13 PM
    Author     : de-arth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Register | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>  
    </head>
    <body class="grey lighten-4">
        <%@ include file="navbar.jspf"%>
        <div class="row">
            <div class="col l6 offset-l3 m8 offset-m2">
                <form id="register" class="card white">
                    <div class="card-content">
                        <span class="card-title center-align"> Register An Account </span>
                        <div class="row">
                            <div class="input-field col l12 m12">
                                <input id="name" class="validate" type="text" name="name" required/>
                                <label for="name"> Name </label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col l12 m12">
                                <input id="email" class="validate" type="email" name="email" required/>
                                <label for="email"> E-mail </label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col l6 m6">
                                <input id="pass" class="validate" type="password" name="password" required/>
                                <label for="pass"> Password </label>
                            </div>
                            <div class="input-field col l6 m6">
                                <input id="conf_pass" class="validate" type="password" name="conf_password" required/>
                                <label for="conf_pass"> Confirm Password </label>
                            </div>
                        </div>
                        <div class="center-align">
                            <button class="btn waves-effect waves-light btn-large" type="submit">Submit
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>

        <script>
            let form = $("#register");
            let message = $("#message");
            console.log("egegeg");
            form.on("submit", event => {
                event.preventDefault();
                event.stopPropagation();
                console.log("aaa");
                if ($("#pass").val() !== $("#conf_pass").val()) {
                    alert("Passwords do not match.");
                    return false;
                }
                $.ajax({
                    type: "POST",
                    url: "serve_register",
                    data: form.serializeArray(),
                    success: data => {
                        console.log(data);
                        M.toast({
                            html: data.message,
                            completeCallback: function () {
                                if (data.status === true)
                                    window.location.href = "index.jsp";
                            }
                        });
                    },
                    error: err => {
                        M.toast({
                            html: "There has been a server error. Please try again."
                        });
                        console.log(err);
                    }
                });
                return false;
            });
        </script>
    </body>
</html>
