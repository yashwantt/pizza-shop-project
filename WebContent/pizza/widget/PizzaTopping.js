
dojo.provide("pizza.widget.PizzaTopping");

dojo.require("pizza.ToppingChangeMssg");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");

dojo.declare(
		"pizza.widget.PizzaTopping",
		[dijit._Widget, dijit._Templated],
		{
			templatePath: dojo.moduleUrl("pizza", "templates/PizzaTopping.html"),
			
			constructor: function() {
				this.data = "";
			},
			
			
			postCreate: function() {
				this.inherited(arguments);
//				this.toppingName = this.data.toppingName;
				
				dojo.connect(this.toppingNode, "onclick", dojo.hitch(this, "_addToOrder", this.data.id));	
				
				this.createPhoto();
				
			},
			
			_addToOrder: function(toppingId) {
				dojo.publish("toppingChange",
						[new pizza.ToppingChangeMssg({action: "add", id: toppingId, topName: this.data.toppingName})]);
			},
			
			createPhoto: function(){
				if (!this.photo) {
					this.photo = document.createElement("img");
					this.photo.src = pizza.config.CONTEXT_PATH + this.data.imageUrl;
					dojo.attr(this.photo,"heitht",'50px');
					dojo.attr(this.photo,"width",'50px');
					this.photoCont.appendChild(this.photo);
				}
			}
			
			
		}
);

