<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
         "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Admin</title>
<style type="text/css">
	@import "/dojoroot/dijit/themes/tundra/tundra.css";
	@import "/dojoroot/dojo/resources/dojo.css";
	@import "/dojoroot/dojox/grid/resources/tundraGrid.css";
	@import url(/css/core.css);
	@import "/css/order_report.css";
	@import "/css/adminWelcome.css";
</style>

<%@include file='/WEB-INF/jsp/include/js.jspf' %>

<script type="text/javascript">
	dojo.registerModulePath("pizza", "../../pizza");
    dojo.require("dojo.parser");
    dojo.require("dojox.grid.DataGrid");
    dojo.require("dojo.data.ItemFileWriteStore"); 
    dojo.require("dojox.form.Rating");
    dojo.require("pizza.admin.adminHome");
    dojo.require("pizza.admin.nav");
</script>
    <script type='text/javascript'>
		function initAdminHome() {
			pizza.admin.adminHome.init();
		}
		dojo.addOnLoad(initAdminHome);
	</script>
</head>

<body class="tundra wrapper">

		<div class="pizzaLogo" >
			<img src="../../images/pizza-logo.png"/>
		</div>

	<table id="topNav">
		<tr>
			<td><a href="#products">Manage Products</a>
			</td>
			<td><a href="#status">Manage Orders</a>
			</td>
		</tr>
	</table>

	<div class="mainCont">

		<div id="productCont" class="productCont">
			<div id="initialize_db" > </div>

			<div class="header2">
				<div id="addNewSizeBtn"></div>
				Pizza Sizes
			</div>
			<div id="pizzaSizesTable" ></div>
			
			
			<div class="header2">
				<div id="addNewToppingBtn"></div>
				Pizza Toppings
			</div>
			<div id="pizzaToppingsTable" ></div>



		</div>


		<div id="statusCont" class="reportCont ">
			<span class="filterCont regText">
				<div>Filter orders and double click on status to change.</div>
				<div>
					<span class='label'>Address:</span> <select id="addressFilter"><option
							value="">- all addresses -</option>
					</select>
				</div>
				<div>
					<span class='label'>Status:</span> <select id="statusFilter"><option
							value="">- any status -</option>
					</select>
				</div> </span>
			<div id="ordersReport"></div>
		</div>
	</div>
</body>
</html>




