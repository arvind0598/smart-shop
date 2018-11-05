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
      		<div class="row">
		<div class="col s12 m8 offset-m2 l6 offset-l3">
             <div class="card large">
	     <span> 
		<div style="text-align:center;">   
		<h5>REGISTER</h5></div>      
            <input id="name" class="validate" type="text" name="name" required/>
	     <label for="name">Name</label>
            <input id="email" class="validate" type="email" name="email" required/>
	    <label for="email">E-mail</label>
            <input id="pass" class="validate" type="password" name="password" required/>
	    <label for="pass">Password</label>
            <input id="conf_pass" class="validate" type="password" name="conf_password" required/>
	    <label for="conf_pass">Confirm Password</label>
	    <div style="text-align:center;">
             <button class="btn waves-effect waves-light" type="submit" name="action">Submit
                <i class="material-icons right">send</i>
            </button>
	    </span>
	    </div>
	    </div>
	  </div>
	    </div>
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
                if ($("#pass").val() !== $("#conf_pass").val()) {
                    alert("Passwords do not match.");
                    return false;
                }
                $.ajax({
                    type: "POST",
                    url: "serve_register",
                    data: form.serializeArray(),
                    success: data => {
                        if (data.status === 1)
                            window.location.href = "index.jsp";
                        message.text(data.message);
                    },
                    error: err => {
                        message.text("There has been a server error. Please try again.");
                        console.log(err);
                    }
                });
                return false;
            });
        </script>
    </body>
</html>
