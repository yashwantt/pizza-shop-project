dojo.provide("pizza.admin.manageToppings");

dojo.require("pizza.widget.table.Table");
dojo.require("dijit.form.Button");
dojo.require("pizza.widget.dialog.Dialog");
dojo.require("dojo.io.iframe");

pizza.admin.manageToppings = {
	
	init: function(){
		this.createToppingTable(pizza.context.toppings.toppings);
	},
	
	createToppingTable: function(toppingsJson) {
		this.addNewToppingBtn = new dijit.form.Button({
			label: 'Add New Topping',
			onClick: dojo.hitch(this, this.addNewToppingPopup)
		}, 'addNewToppingBtn');
		
		
		var buildToppingImage = function(rowIdx, model) {
			var row = model.getData(rowIdx);
			
			var photoCont = dojo.create('div',{
				'class' : 'tableRowToppingImage'
			});
			
			dojo.create('img',{
				'height': '30px',
				'width': '30px',
				'src' : pizza.config.CONTEXT_PATH + row.imageUrl
			}, photoCont);
			
			return photoCont;
		};
		
		var buildToppingName = function(rowIdx, model) {
			var row = model.getData(rowIdx);
			var nameCont = dojo.create('div');
			
			nameCont.innerHTML = row.toppingName;

			
			return nameCont;
		};
		
		var buildToppingType = function(rowIdx, model) {
			var row = model.getData(rowIdx);
			var typeCont = dojo.create('div');
			typeCont.innerHTML = row.type;
			return typeCont;
		};
		
		var buildActive = function(rowIdx, model) {
			var row = model.getData(rowIdx);
			var activeCont = dojo.create('div',{
				'class':  row.active? ' active':''
			}); 
				
				
			dojo.create('input',{
				'type': 'checkbox',
				'onclick': 'pizza.admin.manageToppings._handleToppingActiveCheck(this)',
				'value' : row.id,
				'checked' : row.active
			}, activeCont);
			dojo.create('span',{
				'class': 'fontB2 strong',
				innerHTML : ' ACTIVE'
			}, activeCont);
			return activeCont;
		};
		
		var columns = [
		      {
		    	htmlClass: 'toppingImage',
		    	get: buildToppingImage  
		      },         
		               
		      {
//		    	  id:'toppingName',
		    	  get: buildToppingName,
		    	  label: 'Topping Name'
		      }
		      ,
		      {
//		    	id:'type',
		    	get: buildToppingType,
		    	label: 'Topping Type'
		      },
		      {
//		    	  id:'active',
		    	  get: buildActive
		      }
		
		];
		
		this.toppingTable = new pizza.widget.Table({
			json: toppingsJson,
			showHeader: true,
			columns: columns
		}, 'pizzaToppingsTable');
	},
	
	addNewToppingPopup : function() {
		if(!this.addNewPizzaToppingDlg) {
			var title = "Add a new pizza topping";
			var toppingPopupUrl = pizza.config.CONTEXT_PATH + "admin/addPizzaTopping.htm";
			
			var args2 = {
				id : "addNewToppingPopup",
				title : title,
				href: toppingPopupUrl,
				submitAction: dojo.hitch(this,this.addNewToppingSubmit)
			};
			
			this.addNewPizzaToppingDlg = new pizza.widget.Dialog(args2);
			

		}
		
		this.addNewPizzaToppingDlg.show();
		
	},
	
	addNewToppingSubmit: function(){
		var url = pizza.config.CONTEXT_PATH + "admin/addPizzaTopping.htm";
		var bindings = {
			method: 'post',
			url: url,
			handleAs: 'json',
			load: dojo.hitch(this, "handleAddNewToppingResponse"),
			form: document.forms.addPizzaToppingForm
		};
		dojo.io.iframe.send(bindings);
	},

	handleAddNewToppingResponse: function(response){
		
		// server errors
		if(response.status != 0) {
			return;
		}
		if(response.toppings) {
			this.toppingTable.setModel(response.toppings.toppings);
		}
		this.addNewPizzaToppingDlg.closeDialog();
	},
	
	_handleToppingActiveCheck: function(checkbox) {
		var url;
		if (checkbox.checked) {
			dojo.addClass(checkbox.parentNode, "active");
			url = pizza.config.CONTEXT_PATH + "admin/activatePizzaTopping.htm";
		}
		else {
			dojo.removeClass(checkbox.parentNode, "active");
			url = pizza.config.CONTEXT_PATH + "admin/deactivatePizzaTopping.htm";
		}
		var content = {};
		content['pizzaToppingId'] = checkbox.value;
		var bindings = {
			url: url,
			content: content,
			load: function(){
			},
			
		};
		
		dojo.xhrPost(bindings);
	},

};	