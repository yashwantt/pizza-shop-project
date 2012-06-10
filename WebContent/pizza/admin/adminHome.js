dojo.provide("pizza.admin.adminHome");

dojo.require("pizza.admin.products");
dojo.require("pizza.admin.adminReport");

pizza.admin.adminHome = {
	
	init: function(){
		
		pizza.admin.products.init();
		pizza.admin.adminReport.init();
		pizza.admin.nav.init();
		
	}

		
};