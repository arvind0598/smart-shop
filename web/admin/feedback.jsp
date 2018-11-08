<%-- 
    Document   : feedback
    Created on : 9 Nov, 2018, 2:37:13 AM
    Author     : de-arth
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);

    Integer x = (Integer) session.getAttribute("admlogin");
    if (x == null || x < 1) {
        response.sendRedirect("index.jsp");
        return;
    }

    JSONObject feedback = new Project.Process().getAllFeedback();
    request.setAttribute("feedback", feedback);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>View Feedback | S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="../css/materialize.min.css"  media="screen,projection"/>
        <style>
            body {
                display: flex;
                min-height: 100vh;
                flex-direction: column;
            }

            main {
                flex: 1 0 auto;
            }
        </style>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
        <%--<%=feedback.toString()%>--%>
        <div class="container">
            <div class="row">
                <c:forEach var="x" items="${feedback}">
                    <c:set value="${x.value.status}" var="status"/>
                    <c:set value="${status eq 0 ? 'Received' : status eq 1 ? 'Dispatched' : 'Delivered'}" var="orderstring"/>
                    <div class="card small hoverable col l12">
                        <h5> Order #${x.key} </h5>
                        <div class="divider"></div>
                        <p> Status: ${orderstring} </p>
                        <p> Final Amount: ${x.value.bill} </p>
                        <blockquote>
                            ${x.value.feedback}
                        </blockquote>
                        <p> - ${x.value.name} </p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="../js/materialize.min.js"></script>

</html>
