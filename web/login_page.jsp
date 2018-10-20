<%-- 
    Document   : login_page
    Created on : 20 Oct, 2018, 12:47:42 PM
    Author     : de-arth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <div>
<!--            <form id="register">
               <input/> 
            </form>-->
            <form id="login">
                <input id="user" type="text" name="username" required/>
                <input id="pass" type="password" name="password" required/>
                <button> Submit </button>
            </form>
            <p id="message"></p>
        </div>
        
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
                   url : "serve_login",
                   data : form.serializeArray(),
                   success : data => {
                       if(data.status == 1) window.location.href = "index.jsp";
                       message.text(data.message);
                   },
                   error : err => {
                       console.log("error");
                   }
               });
               return false;
            });
        </script>
    </body>
</html>
