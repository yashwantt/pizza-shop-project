dojo.provide("pizza.order");

dojo.require("pizza.widget.OrderTopping");
dojo.require("pizza.ToppingChangeMssg");
//dojo.require("pizza.nav");

pizza.order = {
//		orderUrl: pizza.config.CONTEXT_PATH + "customer/async/placeOrder.htm",
		orderUrl: pizza.config.CONTEXT_PATH + "customerWelcome.htm",
		chosenToppingsNode: [],
		orderDetails: new Object(),
		toppingsNodeId: 'pizzaToppingIds',
		
		
		init: function(){
			this.chosenToppingsNode = dojo.byId(this.toppingsNodeId);
			dojo.subscribe("toppingChange", this, "_updateChosenToppings");
			this.orderDetails.pizzaToppingIds = [];
			
		},
		
		updateChosenSize: function(event) {
			var chosenSize = pizza.landing.chosenSize;
			var index = chosenSize.selectedIndex;
			this.orderDetails.pizzaSizeId = chosenSize[index].value;
			// clear the validation errors if any
			pizza.valObj.validateField('pizzaSizeId');
		},
		
		_chosenToppingsWidgets: {},
		
		_updateChosenToppings: function(mssg) {

			// validate the toppings
			var ptValidatedElement = pizza.valObj.validatedObjects[this.toppingsNodeId];
//			var ptMinNumValid = ptValidatedElement.doValidationOnType('min_num_toppings');

			
			if(mssg.action == "add") {
				var ptMaxNumValid = ptValidatedElement.doValidationOnType('max_num_toppings');
				if (!ptMaxNumValid) {// max error
					// do not add any more
					return;
				}
				
				// ignore if topping is already selected
				if(this._chosenToppingsWidgets[mssg.topName]) {
					console.debug("topping already exists:" + mssg.topName);
					return;
				}
				
				
				var widget = new pizza.widget.OrderTopping({id: mssg.id, topName: mssg.topName});
				this._chosenToppingsWidgets[mssg.topName] = widget;
				this.orderDetails.pizzaToppingIds.push(mssg.id);
				this.chosenToppingsNode.appendChild(widget.domNode);
			}
			if(mssg.action == "delete") {
				console.debug("in pizza.order action delete");
				

				if(this._chosenToppingsWidgets[mssg.topName]) {
					var widget = this._chosenToppingsWidgets[mssg.topName];
					console.debug("found widget to delete:" + widget.data);
					this.chosenToppingsNode.removeChild(widget.domNode);
					widget.destroyRecursive();
					delete this._chosenToppingsWidgets[mssg.topName];
					for(var i = 0; i <= this.orderDetails.pizzaToppingIds.length; i++){
						if (this.orderDetails.pizzaToppingIds[i] == mssg.id){
							this.orderDetails.pizzaToppingIds.splice(i,1);
						}
					}
					
				} 
				else {
					console.debug("could not find the topping to delete:" + mssg.topName);
				}
				
			}
			
			// display validation messages after each change
			pizza.valObj.validateField(pizzaToppingIds);

		}
		,
		
		removeAllChosenToppings: function(){
			for(widgetKey in this._chosenToppingsWidgets) {
				pizza.order._removeTopping(widgetKey);
			};
		},

		_removeTopping: function(widgetKey){
				var widget = this._chosenToppingsWidgets[widgetKey];
				
				var id = widget.id;
				var topName = widget.topName;
				this.chosenToppingsNode.removeChild(widget.domNode);
				widget.destroyRecursive();
				delete this._chosenToppingsWidgets[topName];
				for(var i = 0; i <= this.orderDetails.pizzaToppingIds.length; i++){
					if (this.orderDetails.pizzaToppingIds[i] == id){
						this.orderDetails.pizzaToppingIds.splice(i,1);
					}
				}
		}
		
		
		
//		,
//		
//		
//		showAddressForm: function() {
//			var mainCont = pizza.nav.orderCont;
////			dojo.addClass(mainCont, "hideme");
//			
//			var pizzaSize = dojo.byId("pizzaSize");
//			var chosenSize = pizza.landing.chosenSize;
//			pizzaSize.innerHTML = " " + chosenSize.attr('displayedValue') ;
//
//			var pizzaToppings = dojo.byId("pizzaToppings");
//			var toppingsString = "";
//			for(var top in this._chosenToppingsWidgets) {
//				toppingsString += top + ", ";
//			}
//			if (toppingsString.lastIndexOf(", ")) {
//				toppingsString = toppingsString.substr(0,toppingsString.lastIndexOf(", "));
//			}
//			pizzaToppings.innerHTML = " " + toppingsString + " ";
//			
//			pizza.nav.showContent("address");
////			var addressCont = dojo.byId("addressCont");
////			dojo.removeClass(addressCont, "hideme");
//			
//			
//		}
		
		,	placeOrder: function() {
			var form = dojo.byId("addressCont");
			var formObj = dojo.formToObject(form);
			var order = dojo.mixin(formObj,this.orderDetails);
			var postData = dojo.objectToQuery(order);
			var args = {
					url: this.orderUrl,
					handleAs: "json",
					error: dojo.hitch(this, this.placeOrderFailure),
					load: dojo.hitch(this, this.placeOrderSuccess),
					postData: postData
					
			};
			
		    dojo.xhrPost(args);
			
		},
		placeOrderFailure: function() {
			this.loadServerErrorsIntoContext(responseData);
			pizza.landing.showServerErrors();
			dojo.byId("placeOrderMssg").innerHTML = "Could not place the order.";
		}
		,placeOrderSuccess: function(responseData) {
			this.loadServerErrorsIntoContext(responseData);
			pizza.landing.showServerErrors();
			if(pizza.context.bindErrors == undefined) {
				dojo.byId("placeOrderMssg").innerHTML = "Order placed successfully.";
			}
		},
		loadServerErrorsIntoContext: function(responseData) {
			pizza.context.addObject("bindErrors", responseData);
			
		}

};

dojo.addOnLoad(pizza.order, 'init');


