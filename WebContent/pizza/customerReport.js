dojo.provide("pizza.customerReport");

pizza.customerReport = {
	
	ordersUrl: pizza.config.CONTEXT_PATH + "getCustomerOrders.htm",
	orderGrid: null,
	orderStore: null,
	
	init: function() {
		

		
		this.orderStore = new dojo.data.ItemFileWriteStore({
			url: null,
			clearOnClose:true
			
		});
		
		var layout = [
		                {name: 'Status', field:"status", width:"150px"},
		                {name: 'Size', field:'size', width:"100px"},
		                {name: 'Toppings', field:'toppings', width:"auto"}
		            ];
		
		this.orderGrid = new dojox.grid.DataGrid({
	    	store: this.orderStore, 
		    clientSort: true,
		    structure: layout,
		    query: {id:'*'}
		},"ordersReport");
//		this.orderGrid.startup();
	},
	loadOrders: function(){

		// is validatedObjects initialized?
//		var validAddress = !(pizza.valObj.validatedObjects == null);
//		if(validAddress) {
		var	validAddress = pizza.valObj.validateAllFields(dojo.byId("addressCont"), false);
//		}

		if(validAddress) {
			
			var form = dojo.byId("addressCont");
			var formObj = dojo.formToObject(form);
			var query = dojo.objectToQuery(formObj);
			this.orderStore.url = this.ordersUrl + "?" + query;
			this.orderStore.close();
//			pizza.customerReport.orderStore.close();
			pizza.customerReport.orderStore.fetch();
			pizza.customerReport.orderGrid._refresh()
//			this.orderStore.fetch();
		}
		else {
			// hide the status grid if no address is there
			dojo.addClass(pizza.nav.statusCont, "hideme");
		}
		
		
	}
	,
	showCustomerAddress: function() {
		var custAdd = dojo.byId("customerAddress");
		var form = dojo.byId("addressCont");
		var formObj = dojo.formToObject(form);
		custAdd.innerHTML = this.addressToString(formObj);
	},
	
	addressToString: function(address) {
		var str = '';
		str += address.streetAddress + '<br\>';
		str += address.city + '<br\>';
		str += address.state + ', ' + address.zip + '<br\>';
		return str;
	},
	
	objToString: function(obj) {
	    var str = '';
	    for (var p in obj) {
	        if (obj.hasOwnProperty(p)) {
	            str += p + ': ' + obj[p] + '<br\>';
	        }
	    }
	    return str;
	}

};

