dojo.provide("pizza.admin.adminReport");

pizza.admin.adminReport = {
	
	ordersUrl: pizza.config.CONTEXT_PATH + "getAllOrders.htm",
	orderGrid: null,
	init: function() {
		this.orderStore = new dojo.data.ItemFileWriteStore({
			url: this.ordersUrl
		});
		
		dojo.connect(this.orderStore,"onSet", this._updateOrder);
		
		var layout = [
		                {name: 'Address', field:"address", width:"100px"},
		                {name: 'Status', field:"status", width:"150px"
		                 , editable: true, type: dojox.grid.cells.Select
		                 , options: pizza.context.statusNames
		                 , values: pizza.context.statusNames
		                },
		                {name: 'Size', field:'size', width:"100px"},
		                {name: 'Toppings', field:'toppings', width:"auto"},
		                {name:'', field:'',width:"6px"}
		            ];
		
		this.orderGrid = new dojox.grid.DataGrid({
	    	store: pizza.admin.adminReport.orderStore, 
		    clientSort: true,
		    structure: layout,
		    query: {id:'*'},
		    sortInfo: 1
		    
		},"ordersReport");
		
		// load the address filter
		this.loadFilter({
			attribute: 'address',
			filterNodeId: 'addressFilter'
		});
		
		// load the status filter
		this.loadFilter({
			attribute: 'status',
			filterNodeId: 'statusFilter'
		});
		
		this.orderGrid.startup();
		
		var addressFilterNode = dojo.byId("addressFilter");
		dojo.connect(addressFilterNode, 'change', pizza.admin.adminReport, this.applyAllFilters);
		
		var statusFilterNode = dojo.byId("statusFilter");
		dojo.connect(statusFilterNode, 'change', pizza.admin.adminReport, this.applyAllFilters);

	},
	
	_updateOrder: function(item,attribute,oldValue,newValue) {
		dojo.xhrPost({
			url: "changeOrderStatus.htm"
			, content: {
				id: this.getValue(item, "id")
				,status: this.getValue(item, "status")
			}
			,error: function(){
				alert("error saving the new status");
			}
			,load: function(){
				console.debug("status changed successfully");
				pizza.admin.adminReport.applyAllFilters();
			}
		});
	},
	
	loadFilter: function(args){
		
		var sortAttribute = args.attribute;
		var filterNodeId = args.filterNodeId;
		
		var filterHash = {};
		
		this.orderStore.fetch({
			query: {id: '*'},
			onItem: function(theItem) {
				filterHash[theItem[sortAttribute]] = true;
				
			},
			onComplete: function() {
				var sortedCriteria = [];
				for (criteriaName in filterHash) {
					sortedCriteria.push(criteriaName);
				}
				
				
				sortedCriteria.sort();
				
				var filterNode = dojo.byId(filterNodeId);
				
			
//				// then load the rest of options
				var filterBoxOptions =  filterNode.options;
				dojo.forEach(sortedCriteria, function(criteriaName){
					filterBoxOptions[filterBoxOptions.length] = new Option(criteriaName);
				});
				
			}
		});
	},
	

	
	applyAllFilters: function() {
		var query = {};
		this.getAddressCriteria(query);
		this.getStatusCriteria(query);
		this.orderGrid.filter(query);
	},

	
	getAddressCriteria: function(query) {
		var filterNode = dojo.byId("addressFilter");
		var selectIndx = filterNode.selectedIndex;
		if (0 != selectIndx) {// the first option is all
			var currentValue = filterNode.options[selectIndx].text;
			dojo.mixin(query,{address : currentValue});
		}
		else {
			dojo.mixin(query,{address : '*'});
		}
	},
	
	getStatusCriteria: function(query) {
		var filterNode = dojo.byId("statusFilter");
		var selectIndx = filterNode.selectedIndex;
		if (0 != selectIndx) {// the first option is all
			var currentValue = filterNode.options[selectIndx].text;
			dojo.mixin(query,{status : currentValue});
		}
		else {
			dojo.mixin(query,{status : '*'});
		}
	}	
		
};

