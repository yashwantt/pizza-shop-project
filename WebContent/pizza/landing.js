
dojo.provide("pizza.landing");
//dojo.require("dijit.form.Select");


pizza.landing = 
		
		{
			
			sizeUrl: pizza.config.CONTEXT_PATH + "customer/async/getSizes.htm",
			toppingUrl: pizza.config.CONTEXT_PATH + "customer/async/getToppings.htm",
			submitPizzaBtn: null,
			chosenSize: null,
			
			init : function(args) {
				dojo.addClass(dojo.byId("mainCont"),"loading");

				this.loadSizes();
				this.loadToppings();
				
				this.setupAddressForm();
				
				
				this.submitPizzaBtn = new dijit.form.Button({
					label: "Continue",
					onClick: this._handleClickPizzaBtn
				},"submitPizza");
				
				this.resetPizzaBtn = new dijit.form.Button({
					label: "Start Over",
					onClick: this._handleResetPizzaBtn
				},"resetPizza");
//				this.hookupFormFieldsWithFinishButton();

				
				pizza.customerReport.init();
				
				
				
//				pizza.valObj = new pizza.validation.Validator(pizza._valData);
//				if (pizza.addCustomValidation) {
//					pizza.addCustomValidation(pizza.valObj);
//				}
				
				
				pizza.nav.init();
				
//				this.initCustomValidation();
				// If we have any bindErrors here, then we've already submitted the page and had errors.
				this.showServerErrors();
			},
			
			_handleClickPizzaBtn: function() {
				var validSize = pizza.valObj.validateField('pizzaSizeId');
				var validTops = pizza.valObj.validateField('pizzaToppingIds');
				var validPizza = validSize && validTops;
				if(!validPizza){
					return;// don't leave this tab unless pizza order is valid
				}
				var validAddress = pizza.valObj.validateAllFields(dojo.byId("addressCont"), false);
				if(validAddress) {// submit the order if address is already filled
					pizza.order.placeOrder();
				} 
				else {// otherwise open the address tab for the user
					pizza.nav.showContent('address');
				}
			},
			
			_handleResetPizzaBtn: function() {
				pizza.landing.chosenSize.selectedIndex = 0;
				pizza.order.removeAllChosenToppings();
				dojo.byId("placeOrderMssg").innerHTML = '';
			},
			
			showServerErrors: function() {
				this.clearAllValidationErrors();
				if (pizza.context.bindErrors != null) {
					for(var i = 0; i < pizza.context.bindErrors.length; i++) {
						var error = pizza.context.bindErrors[i];
						var node = dojo.byId(error.fieldName);
						pizza.valObj.defaultDisplayErr(node, error.fieldError);
					}
				}
			},
			
			clearAllValidationErrors: function() {
				for(var fieldId in pizza.valObj.validatedObjects) {
					pizza.valObj.validatedObjects[fieldId].clear();
				}
			},
			
			
			
//			hookupFormFieldsWithFinishButton: function() {
//				dojo.query("input[type='text']", dojo.byId("addressCont")).onkeyup(
//						function() {
//							pizza.landing.changeFinishButton();
//						}
//				)
//			},
			
//			changeFinishButton: function() {
//				var validAllFields = pizza.valObj.validateAllFields(dojo.byId("addressCont"), false);
//				this.submitAddressBtn.set('disabled', !validAllFields);
//			},
			
			updateOrderBtnTxt: function(){
				var validAddress = pizza.valObj.validateAllFields(dojo.byId("addressCont"), false);
				if (validAddress && this.submitPizzaBtn){
					this.submitPizzaBtn.set('label', 'Place the order');
				}
			},
			
//			updateAddressBtnLabel: function() {
//				var validOrder = true;
//				if (validOrder && this.submitAddressBtn){
//					this.submitAddressBtn.set('label', 'Submit Address');
//				}
//			},
			
			setupAddressForm : function() {
				this.submitAddressBtn = new dijit.form.Button({
					label: "Submit Address",
					onClick: function(){
						var validAddress = pizza.valObj.validateAllFields(dojo.byId("addressCont"), true);
						if(validAddress) {
							pizza.nav.showContent('order');
//							pizza.order.placeOrder();
						}
					}
				},"submitAddress");
			},
			
			
			loadSizes: function() {
				
//				this.chosenSize = new dijit.form.Select({
//					name: 'chosenSize',
//					options:[]
//				}, "pizzaSizeId");
				this.chosenSize = dojo.byId('pizzaSizeId');
				
				
//				this.chosenSize = dojo. dijit.form.Select({
//					name: 'chosenSize',
//					options:[{label:'-Pizza Size-', value:''}]
//				}, "pizzaSizeId");

//				this.chosenSize.onChange = dojo.hitch(pizza.order,pizza.order.updateChosenSize);
				dojo.connect(this.chosenSize, "onchange", pizza.order,"updateChosenSize");
				
				var content = {};
				var args = {
						url: this.sizeUrl,
						handleAs: "json",
						load: dojo.hitch(this, this.showSizes),
						content: content
				};
				
			    dojo.xhrGet(args);
				
			},
			
			
			showSizes : function(data) {
				
				this.chosenSize.options[0] = new Option("- pizza size -",'',true);
				
				if(data && data.resultsCount ) {
					for(var i=1;i<=data.resultsCount;i++){
						this.chosenSize.options[i] = new Option(data.sizes[i-1].sizeName,data.sizes[i-1].id,false,false);
					} 
				};
//				dojo.removeClass(this.chosenSize.domNode, "hideme");
			},

			loadToppings : function() {
				
				var content = {};
				var args = {
						url: this.toppingUrl,
						handleAs: "json",
						load: dojo.hitch(this, this.showToppings),
						content: content
				};
				
			    dojo.xhrGet(args);
				
			},
			
			showToppings : function(data) {
				
		    	if(data && data.resultsCount > 0) {
		    		this.createPizzaToppings(data);
//		    		dojo.removeClass(dojo.byId("mainCont"),"hideme");
		    	}	
		    	
				dojo.removeClass(dojo.byId("mainCont"),"loading");
//				dojo.removeClass(dojo.byId("chosenItems"), "hideme");

			}
			
			,
			
			createPizzaToppings: function(data) {
				
				var toppings = data.toppings;
	
				var meatCont1 = dojo.byId("meatCheeseToppings1");
				dojo.addClass(meatCont1, "hideme"); 
				var meatCont2 = dojo.byId("meatCheeseToppings2");
				dojo.addClass(meatCont2, "hideme"); 

				var vegCont1 = dojo.byId("vegToppings1");
				dojo.addClass(vegCont1, "hideme"); 
				var vegCont2 = dojo.byId("vegToppings2");
				dojo.addClass(vegCont2, "hideme"); 
				
				
				var start = 0;
				var end = toppings.length;

				var widget;
				var putInMeatCont1 = true;
				var putInVegCont1 = true;
				for (var i = start; i < end; i++) {
					topping = toppings[i];
					widget = new pizza.widget.PizzaTopping({ data: topping });
					if(topping.type == 'MeatsCheese') {
						if(putInMeatCont1) {
							meatCont1.appendChild(widget.domNode);
							putInMeatCont1 = false;
						}
						else {
							meatCont2.appendChild(widget.domNode);
							putInMeatCont1 = true;
						}
					}
					else {
						if(putInVegCont1) {
							vegCont1.appendChild(widget.domNode);
							putInVegCont1 = false;
						}
						else {
							vegCont2.appendChild(widget.domNode);
							putInVegCont1 = true;
						}
					}
				}
				
				dojo.removeClass(meatCont1, "hideme");
				dojo.removeClass(meatCont2, "hideme");
				dojo.removeClass(vegCont1, "hideme");
				dojo.removeClass(vegCont2, "hideme");
			}
			
			
		};


dojo.addOnLoad(pizza.landing, 'init');
