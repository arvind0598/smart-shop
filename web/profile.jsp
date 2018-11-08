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

    Integer x = (Integer) session.getAttribute("login");
    if (x == null || x < 1) {
        response.sendRedirect("index.jsp");
        return;
    }
    JSONObject details = new Project.Process().getCustomerDetails(x);
    session.setAttribute("details", details);

    JSONObject orders = new Project.Process().getOrderHistory(x);
    session.setAttribute("orders", orders);
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
            <h4> ${details.name} </h4>
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

        <div class="row">
            <div class="col l6">
                <form id="change_pass" onsubmit="return changePass()" class="card medium hoverable">
                    <div class="card-content">
                        <span class="card-title"> Change Password </span>
                        <div class="row">
                            <div class="input-field col l8">
                                <input type="password" id="curr_pass" name="curr_pass" class="validate" required/>
                                <label for="curr_pass">Current Password</label>
                            </div>
                        </div>
                        <div class="row"> 
                            <div class="input-field col l8">
                                <input type="password" id="new_pass" name="new_pass" class="validate" pattern =".{6,}" title="Six or more characters" required/>
                                <label for="new_pass">New Password</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="input-field col l8">
                                <input type="password" id="conf_pass" class="validate" required/>
                                <label for="conf_pass">Confirm New Password</label>
                            </div>
                        </div>
                    </div>
                    <div class="card-action">
                        <button class="waves-effect waves-light btn">Change Password</button>
                    </div>
                </form>
            </div>

            <div class="col l6">
                <form id="change_add" onsubmit=" return changeAddress()" class="card medium hoverable">
                    <div class="card-content">
                        <span class="card-title"> Change Address </span>
                        <div class="row">
                            <div class="input-field col l12">
                                <textarea id="address" name="address" class="materialize-textarea" required></textarea>
                                <label for="address"> New Address </label>
                            </div>
                        </div>

                    </div>
                    <div class="card-action">
                        <button class="waves-effect waves-light btn">Change Address</button>
                    </div>
                </form>
            </div>

            <div class="container">

            <h4> Order History </h4>
            <div class="row">
            <c:forEach items="${orders}" var="ord">
                
                    <div class="col l6">
                        <div class="card small hoverable">
                            <div class="card-content">		
                                <h5>Order no.: ${ord.key}</h5>
                                <p>Bill: ${ord.value.bill}</p>
                                <c:choose>
                                    <c:when test="${ord.value.status eq 0}">
                                        <p>Status: Received</p>
                                    </c:when>
                                    <c:when test="${ord.value.status eq 1}">
                                        <p>Status: Dispatched</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Status: Delivered</p>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${ord.value.feedback eq 0}">
                                        <div class="input-field col m7">
                                            <textarea id="feed" name="feedback" class="materialize-textarea" required></textarea>
                                            <label for="feed"> Feedback </label>
                                        </div>
                                        <div class="input-field col m2">
                                            <button class="waves-effect waves-light btn">Submit Feedback</button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <h5>Feedback Submitted</h5>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                
            </c:forEach>
                </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/materialize.min.js"></script>

        <script>
                    let passForm = $("#change_pass");
                    let addressForm = $("#change_add");

                    const changePass = () => {
                        if ($("#new_pass").val() !== $("#conf_pass").val()) {
                            alert("Passwords do not match.");
                            return false;
                        }
                        $.ajax({
                            type: "POST",
                            url: "serve_changepass",
                            data: passForm.serializeArray(),
                            success: data => {
                                alert(data.message);
                            },
                            error: err => {
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
                            data: addressForm.serializeArray(),
                            success: data => {
                                alert(data.message);
                                window.location.reload(true);
                            },
                            error: err => {
                                alert("There has been an error.");
                                console.log(err);
                            }
                        });
                        return false;
                    }

        </script>

    </body>
</html>
