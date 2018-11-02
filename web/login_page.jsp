<%-- 
    Document   : login_page
    Created on : 20 Oct, 2018, 12:47:42 PM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
    
    Integer x = (Integer)session.getAttribute("login");
    if(x != null && x > 0) {
        response.sendRedirect("index.jsp");
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Login | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>  
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <form id="login" class="container row valign-wrapper">
            <div class="input-field col s6">
                <input id="user" type="email" name="useremail" class="validate" required/>
                <label for="useremail"> Email ID </label>
            </div>
            <div class="input-field col s6">
                <input id="pass" type="password" name="password" class="validate" required/>
                <label for="password"> Password </label>
            </div>
            <button class="btn waves-effect waves-light" type="submit" name="action">Submit
              <i class="material-icons right">send</i>
            </button>        
        </form>
        <p id="message"></p>
        
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>
        
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
                       if(data.status > 0) {
                           window.location.href = "<%=session.getAttribute("currentpage")%>";
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
