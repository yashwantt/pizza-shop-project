<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
  <head>
    <title>Welcome to Admin Page</title>
  </head>
  <body>
  	<div><a href="j_acegi_logout">Logoff</a></div>
	<ul>
	<LI><A HREF="initialize_db.htm"> Initialize the DB </A></LI> 
	<LI><A HREF="change_product.htm"> Change Pizza Size or Toppings</A></LI> 
	<LI><A HREF="advance_day.htm"> Advance day</A></LI>
	<LI><A HREF="mark_order_ready.htm"> Make order ready</A></LI>
	<LI><A HREF="welcome.htm"> Back to Homepage</A></LI>
	</ul>
  </body>
</html>
