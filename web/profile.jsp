<%-- 
    Document   : profile
    Created on : 26 Oct, 2018, 2:17:20 PM
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
    if(x == null || x < 1) {
        response.sendRedirect("index.jsp");
        return;
    }
    JSONObject details = new Project.Process().getCustomerDetails(x);
    session.setAttribute("details", details);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Profile | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>  
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <div>
            <p> Name: ${details.name} </p>
            <p> Email: ${details.email} </p>
            <p> Points: ${details.points} </p>
            <c:choose>
                <c:when test="${details.address ne null}">
                    <p> Address: ${details.address} </p>
                </c:when>
                <c:otherwise>
                    <p><b> Please Enter Address to place order. </b></p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <h4> Change Password </h4>
        <form id="change_pass" onsubmit="return changePass()">
            <input type="password" id="curr_pass" name="curr_pass" placeholder="Current Password" required/>
            <input type="password" id="new_pass" name="new_pass" placeholder="New Password" required/>
            <input type="password" id="conf_pass" placeholder="Confirm New Password" required/>
            <button> Change Password </button>
        </form>
        
        <h4> Change Address </h4>
        <form id="change_add" onsubmit=" return changeAddress()">
            <textarea id="address" name="address" required></textarea>
            <button> Change Address </button>
        </form>
        
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>
        
        <script>
            let passForm = $("#change_pass");
            let addressForm = $("#change_add");
            
            const changePass = () => {
                if($("#new_pass").val() !== $("#conf_pass").val()) {
                   alert("Passwords do not match.");
                   return false;
               }
                $.ajax({
                    type: "POST",
                    url: "serve_changepass",
                    data : passForm.serializeArray(),
                    success: data => {
                        alert(data.message);
                    },
                    error : err => {
                        alert("There has been an error.");
                        console.log(err);
                    }
                });
                return false;                
            }
            
            const changeAddress = () => {
                $.ajax({
                    type: "POST",
                    url: "serve_changeadd",
                    data : addressForm.serializeArray(),
                    success: data => {
                        alert(data.message);
                        window.location.reload(true);
                    },
                    error : err => {
                        alert("There has been an error.");
                        console.log(err);
                    }
                });
                return false;                
            }

        </script>
        
    </body>
</html>
