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
    <body>
        <%@ include file="navbar.jspf"%>
        <form id="register">    
            <input id="name" type="text" name="name" placeholder="Name" required/>
            <input id="email" type="email" name="email" placeholder="Email" required/>
            <input id="pass" type="password" name="password" placeholder="Enter Password" required/>
            <input id="conf_pass" type="password" name="conf_password" placeholder="Confirm Password" required/>
            <button> Submit </button>
        </form>
        <p id="message"></p>
        
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>
        
        <script>
            let form = $("#register");
            let message = $("#message");
            form.on("submit", event => {
               event.preventDefault();
               event.stopPropagation();
               if($("#pass").val() !== $("#conf_pass").val()) {
                   alert("Passwords do not match.");
                   return false;
               }
               $.ajax({
                   type : "POST",
                   url : "serve_register",
                   data : form.serializeArray(),
                   success : data => {
                       if(data.status === 1) window.location.href = "index.jsp";
                       message.text(data.message);
                   },
                   error : err => {
                       message.text("There has been a server error. Please try again.");
                       console.log(err);
                   }
               });
               return false;
            });
        </script>
    </body>
</html>
