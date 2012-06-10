dojo.provide("pizza.admin.manageSizes");

dojo.require("pizza.widget.table.Table");
dojo.require("dijit.form.Button");
dojo.require("pizza.widget.dialog.Dialog");

pizza.admin.manageSizes = {
	
	init: function(){

		this.createSizeTable(pizza.context.pizzaSizes.sizes);
		
	},
	
	createSizeTable: function(sizesJson) {
		this.addNewSizeBtn = new dijit.form.Button({
			label: 'Add New Size',
			onClick: dojo.hitch(this, this.addNewSizePopup)
		}, 'addNewSizeBtn');
		
		var buildPizzaName = function(rowIdx, model) {
			var row = model.getData(rowIdx);
			var nameCont = dojo.create('div', {
				innerHTML : row.sizeName
			});
			return nameCont;
		};
		
		var buildActive = function(rowIdx, model) {
			var row = model.getData(rowIdx);
			var activeCont = dojo.create('div',{
				'class':  row.active? ' active':''
			}); 
				
				
			dojo.create('input',{
				'type': 'checkbox',
				'onclick': 'pizza.admin.manageSizes._handleSizeActiveCheck(this)',
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
		    	  id:'sizeName',
		    	  get: buildPizzaName,
		    	  label: 'Size Name'
		      },
		      {
		    	  id:'active',
		    	  get: buildActive,
		      }
		
		];
		
		this.sizeTable = new pizza.widget.Table({
			json: sizesJson,
			showHeader: true,
			columns: columns
		}, 'pizzaSizesTable');
// this.sizeTable.startUp();
	},

	
	addNewSizePopup : function() {
		if(!this.addNewPizzaSizeDlg) {
			var title = "Add a new pizza size";
			var sizePopupUrl = pizza.config.CONTEXT_PATH + "admin/addPizzaSize.htm";
			
			var args2 = {
				id : "addNewSizePopup",
				title : title,
				href: sizePopupUrl,
				submitAction: dojo.hitch(this,this.addNewSizeSubmit)
			};
			
			this.addNewPizzaSizeDlg = new pizza.widget.Dialog(args2);
			

		}
		
		this.addNewPizzaSizeDlg.show();
		
	},
	
	addNewSizeSubmit: function(){
		var url = pizza.config.CONTEXT_PATH + "admin/addPizzaSize.htm";
		var bindings = {
			url: url,
			handleAs: "json",
			load: dojo.hitch(this, "handleAddNewSizeResponse"),
			form: document.forms.addPizzaSizeForm
		};
		dojo.xhrPost(bindings);
	},
	
	handleAddNewSizeResponse: function(response){
		
		// server errors
		if(response.status != 0) {
			return;
		}
		if(response.pizzaSizes) {
			this.sizeTable.setModel(response.pizzaSizes.sizes);
		}
		this.addNewPizzaSizeDlg.closeDialog();
	},

	_handleSizeActiveCheck: function(checkbox) {
		var url;
		if (checkbox.checked) {
			dojo.addClass(checkbox.parentNode, "active");
			url = pizza.config.CONTEXT_PATH + "admin/activatePizzaSize.htm";
		}
		else {
			dojo.removeClass(checkbox.parentNode, "active");
			url = pizza.config.CONTEXT_PATH + "admin/deactivatePizzaSize.htm";
		}
		var content = {};
		content['pizzaSizeId'] = checkbox.value;
		var bindings = {
			url: url,
			content: content,
			load: function(){
				console.debug("_handleSizeActiveCheck : success");
			},
			
		};
		
		dojo.xhrPost(bindings);
	}
};	
