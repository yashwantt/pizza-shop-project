<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Welcome to Pizza Order Shop</title>
    <style type="text/css">
        @import "/dojoroot/dijit/themes/tundra/tundra.css";
		@import "/dojoroot/dojox/grid/resources/tundraGrid.css";
        @import "/dojoroot/dojo/resources/dojo.css";
        @import "/css/pizza.css";
        @import "/css/order_report.css";
         @import "/css/customerWelcome.css"; 
        
    </style>
</head>


<body class="tundra">

<div id="wrapper" class="wrapper">
<%@include file='/WEB-INF/jsp/include/js.jspf' %>

    <script type="text/javascript">
   		dojo.registerModulePath("pizza", "../../pizza");
	       dojo.require("dojo.parser");
	       dojo.require("dijit.form.Button");
	       dojo.require("dijit.Menu");
	       dojo.require("dijit.Toolbar");
	       dojo.require("pizza.widget.PizzaTopping");
	       dojo.require("dijit.form.ValidationTextBox");
	       dojo.require("dojo.data.ItemFileWriteStore"); 
	       dojo.require("dojox.grid.DataGrid");
	       dojo.require("pizza.nav");
	       dojo.require("pizza.order");
	       dojo.require("pizza.customerReport");
	       dojo.require("pizza.landing");
    </script>


    
    <script type='text/javascript'>

		function initPizza() {
// 			pizza.landing.init();
		}
	
		dojo.addOnLoad(initPizza);
	</script>


<!-- Define relative links for various cases: 
	c:url handles URL rewriting if needed to maintain session -->
<c:url value="addressForm.htm" var="addressFormURL"/>
<c:url value="orderForm.htm" var="orderFormURL"/>
<c:url value="pizzaStatus.htm" var="pizzaStatusURL"/>
<!-- use c:url to figure out context-relative URL -->
<c:url value="/welcome.htm" var="homeURL"/>



<div class="pizzaLogo" >
<img src="../../images/pizza-logo.png"/>
</div>

<table  id="topNav">
	<tr>
		<td><a href="#order">Your order</a></td>
		<td><a href="#address">Your Address</a></td>
		<td><a href="#status">Past Orders</a></td>
	</tr>
</table>

<div id="mainCont" class="mainCont">
		
	<div id="orderCont">
		<div id="availableToppings">
			<div id="meatWrapper">
				<div id="meatTitle">Meats and Cheese</div>
				<div id="meatCheeseToppings1"></div>
				<div id="meatCheeseToppings2"></div>
			</div>
			<div id="vegWrapper">
				<div id="vegTitle">Fresh Cut Veggies</div>
				<div id="vegToppings1"></div>
				<div id="vegToppings2"></div>
			</div>
		</div>
		<div id="chosenItems" >
			<div class="chosenText">Your Pizza Size</div>
			<div id="pizzaSizeId.errors" class="error"></div>
			<select id="pizzaSizeId" class="hideme"></select>
			<div class="chosenText">Your Toppings</div>
			<div id="pizzaToppingIds"></div>
		</div>
		<div id="resetOrder"></div>
		<div id="submitPizza"></div>
		<span id="resetPizza"></span>
		<div id="placeOrderMssg" class="regText"></div>
		
	</div>
		
	<form id="addressCont" class="hideme" method="post">
		<div>Please provide an address for delivery.</div>
		<div><label for="streetAddress">Street Address:</label>
		<input id="streetAddress" name="streetAddress" type='text'/></div>
		
		<div><label for="city">City:</label>
		<input id="city" name="city" type='text'/></div>
		
		<div><label for="state">State:</label>
		<input id="state" name="state" type='text'/></div>
		
		<div><label for="zip">Zip:</label>
		<input id="zip" name="zip" type='text'/></div>
		
		<div id="submitAddress"></div>
	</form>
	
	<div id="statusCont" class="hideme">
		<div class="regText">Orders for address:</div>
		<div id="customerAddress" class="regText"></div>
		<div id="ordersReport"></div>
	</div>
	
</div>


</div>
</body>
</html>
