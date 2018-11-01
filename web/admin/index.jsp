<%-- 
    Document   : index
    Created on : 1 Nov, 2018, 9:52:21 PM
    Author     : de-arth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Admin Login | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <form id="login">
            <input id="user" type="email" name="useremail" placeholder="Email ID" required/>
            <input id="pass" type="password" name="password" placeholder="Password" required/>
            <button> Submit </button>
        </form>
        <p id="message"></p>
        
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js">          
        </script>
        
        <script>
            let form = $("#login");
            let message = $("#message");
            form.on("submit", event => {
               event.preventDefault();
               event.stopPropagation();
               $.ajax({
                   type : "POST",
                   url : "../serve_admlogin",
                   data : form.serializeArray(),
                   success : data => {
                       if(data.status > 0) {
                           window.location.href = "landing.jsp";
                       }
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
