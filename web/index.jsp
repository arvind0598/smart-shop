<%--
    Document   : index
    Created on : 19 Oct, 2018, 11:59:19 AM
    Author     : de-arth
--%>

<%@page import="org.json.simple.JSONArray"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
    JSONObject categories = new Project.Process().getCategories();
    request.setAttribute("categories", categories);
    session.setAttribute("currentpage", "index.jsp");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>S Mart</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>
    </head>
    <body>
        <%@ include file="navbar.jspf"%>
	
	<div class="row">
    	 <div class="col m3">
	  <h4>Categories</h4>
           <div class="collection">
            <c:forEach items="${categories}" var="cat">
                 <a href="category.jsp?id=${cat.key}" class="collection-item"> ${cat.value}</a>
		
            </c:forEach>
	   </div>
	</div>
	<div class="col m3">
         <div class="card small">
          <div class="card-image">
          <img src="https://www.franchiseindia.com/uploads/content/ri/art/lodi-the-garden-restaurant-start-60f6b1bb83.jpg">
          </div>
          <div class="card-content">
	  <span class="card-title">Fast Home Delivery</span>
          <p>Products delivered within 24 hours of order placement.</p>
         </div>
        </div>
       </div>	
       <div class="col m3">
        <div class="card small">
         <div class="card-image">
          <img src="http://www.dorusomcutean.com/wp-content/uploads/2017/06/Best-deals-simcard.jpg">
        </div>
        <div class="card-content">
	<span class="card-title">Best Prices And Deals</span>
        <p>Enjoy daily deals and offers on the best quality products.</p>
        </div>
      </div>
    </div>
    <div class="col m3">
     <div class="card small">
       <div class="card-image">
         <img src="https://cdn0.tnwcdn.com/wp-content/blogs.dir/1/files/2016/03/shutterstock_242438494-1200x676.jpg">
       </div>
       <div class="card-content">
       <span class="card-title">You Speak, We Listen!</span>
       <p>Your feedbacks are very important to us.</p>
       </div>
     </div>
    </div>
   </div>
   <div class="row">
    <div class="col m3"></div>
     <div class="col m3">
      <div class="card small">
       <div class="card-image">
          <img src="https://cmkt-image-prd.global.ssl.fastly.net/0.1.0/ps/386453/1160/772/m1/fpnw/wm1/gurza_01128-.jpg?1425456055&s=3bd789947477aac05f7d9aa57284933b">
        </div>
        <div class="card-content">
	<span class="card-title">Wide Product Range</span>
        <p>From groceries to clothing, we've got you covered!</p>
        </div>
      </div>
    </div>
    <div class="col m3">
     <div class="card small">
      <div class="card-image">
       <img src="http://bpic.588ku.com//element_origin_min_pic/17/09/02/c525ae37b545af5615d778937d99409c.jpg">
      </div>
      <div class="card-content">
      <span class="card-title">Easy Payments</span>
      <p>Pay at you convenience, using your preferred method of payments.</p>
      </div>
     </div>
    </div>
    <div class="col m3">
     <div class="card small">
      <div class="card-image">
       <img src="https://png.pngtree.com/element_origin_min_pic/16/08/18/1757b581c502064.jpg">
      </div>
      <div class="card-content">
      <span class="card-title">Convenient</span>
      <p>Shop for your favourite products sitting at home.</p>
     </div>
    </div>
   </div>
 </div>
    </body>
</html>
