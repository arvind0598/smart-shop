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
        <link href="main.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <form id="register">    
            <input id="user" type="text" name="username" placeholder="Username" required/>
            <input id="pass" type="password" name="password" required/>
            <input id="conf_pass" type="password" name="conf_password" required/>
            <input id="email" type="email" name="email" required/>
            <button> Submit </button>
        </form>
        <p id="message"></p>
        
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
        </script>
        
        <script>
            let form = $("#register");
            let message = $("#message");
            form.on("submit", event => {
               event.preventDefault();
               event.stopPropagation();
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
