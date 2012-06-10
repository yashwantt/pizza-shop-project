dojo.provide("pizza.admin.products");

dojo.require("dijit.form.Button");
dojo.require("pizza.admin.manageSizes");
dojo.require("pizza.admin.manageToppings");


pizza.admin.products = {
	
	init: function(){

		this.initializeDbBtn = new dijit.form.Button({
			label: 'Reset the Database',
			onClick: this.handleInitDb
		}, 'initialize_db');
		
		pizza.admin.manageSizes.init();
		pizza.admin.manageToppings.init();
	},
	
	handleInitDb: function() {
		var bindings = {
			url: pizza.config.CONTEXT_PATH + "admin/initialize_db.htm"
		};
		dojo.xhrPost(bindings);
	}
	
};	